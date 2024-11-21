package com.epam.homework.service;

import com.epam.homework.repository.UserRepository;
import com.epam.homework.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserService")
class UserServiceTest {

    @Mock private UserRepository userRepository;

    @InjectMocks private UserService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should get an User by id")
    void getUserById() {
        var expectedUser = new User("Roger", "roger@example.com");
        expectedUser.setId(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));
        var user = service.getUserById(1L);
        assertEquals(user.getName(), "Roger");
    }

    @Test
    @DisplayName("should throw an exception when an user is not found")
    public void getUser_throwException() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    service.getUserById(100L);
                });
    }

    @Test
    @DisplayName("should get an User by its email")
    void getUserByEmail() {
        var expected = new User("Rafa", "rafa@email.com");
        expected.setId(2L);
        when(userRepository.findByEmail("rafa@email.com")).thenReturn(Optional.of(expected));
        var user = service.getUserByEmail("rafa@email.com");
        assertEquals(2L, user.getId());
    }

    @Test
    @DisplayName("should throw an exception when an user is not found by email")
    public void getByEmail_throwException() {
        assertThrows(
                NullPointerException.class,
                () -> {
                    service.getUserByEmail("bad@mail.com");
                });
    }

    @Test
    @DisplayName("should get all Users by a name")
    void getUsersByName() {
        var mockUser = new User("foo", "foo@mail.com");
        var mockPage = new PageImpl<>(List.of(mockUser));
        when(userRepository.findByName("foo", PageRequest.of(0, 10))).thenReturn(mockPage);

        var result = service.getUsersByName("foo", 10, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockUser.getEmail(), result.get(0).getEmail());
    }

    @Test
    @DisplayName("should return empty list when no users are found")
    void getByName_returnEmptyList() {
        var mockPage = new PageImpl<User>(Collections.emptyList());
        when(userRepository.findByName("foo", PageRequest.of(0, 10))).thenReturn(mockPage);

        var result = service.getUsersByName("foo", 10, 0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should update an User")
    void updateUser() {
        var mockUser = new User("Roger2", "roger2@email.com");
        mockUser.setId(3L);
        when(userRepository.findById(3L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        var result = service.updateUser(mockUser);
        assertEquals(mockUser, result);
        var fetched = service.getUserById(mockUser.getId());
        assertEquals(mockUser, fetched);
    }

    @Test
    @DisplayName("should delete an User")
    void deleteUser() {
        var mockUser = new User("Test", "test@mail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        assertTrue(service.deleteUser(1L));
        when(userRepository.findById(101L)).thenReturn(Optional.empty());
        assertFalse(service.deleteUser(101L));
    }

    @Test
    @DisplayName("should not allow to repeat email when creating users")
    void createUserFail() {
        var newUser = new User("Test", "roger@email.com");
        when(userRepository.findByEmail("roger@email.com")).thenReturn(Optional.of(newUser));
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    service.createUser(newUser);
                });
    }
}
