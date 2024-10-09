package com.epam.homework4.service;

import com.epam.homework4.dao.UserRepository;
import com.epam.homework4.model.User;
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
        // Arrange
        var expectedUser = new User("Roger", "roger@example.com");
        expectedUser.setId(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        // Act
        var user = service.getUserById(1L);

        // Assert
        assertEquals(user.getName(), "Roger");
    }

    @Test
    @DisplayName("should throw an exception when an user is not found")
    public void getUser_throwException() {
        // Arrange
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> {
                    service.getUserById(100L);
                });
    }

    @Test
    @DisplayName("should get an User by its email")
    void getUserByEmail() {
        // Arrange
        var expected = new User("Rafa", "rafa@email.com");
        expected.setId(2L);
        when(userRepository.findByEmail("rafa@email.com")).thenReturn(Optional.of(expected));

        // Act
        var user = service.getUserByEmail("rafa@email.com");

        // Assert
        assertEquals(2L, user.getId());
    }

    @Test
    @DisplayName("should throw an exception when an user is not found by email")
    public void getByEmail_throwException() {
        // Arrange
        when(userRepository.findByEmail("bad@mail.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> {
                    service.getUserByEmail("bad@mail.com");
                });
    }

    @Test
    @DisplayName("should get all Users by a name")
    void getUsersByName() {
        // Arrange
        var mockUser = new User("foo", "foo@mail.com");
        var mockPage = new PageImpl<>(List.of(mockUser));
        when(userRepository.findByName("foo", PageRequest.of(0, 10))).thenReturn(mockPage);

        // Act
        var result = service.getUsersByName("foo", 10, 0);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockUser.getEmail(), result.get(0).getEmail());
    }

    @Test
    @DisplayName("should return empty list when no users are found")
    void getByName_returnEmptyList() {
        // Arrange
        var mockPage = new PageImpl<User>(Collections.emptyList());
        when(userRepository.findByName("foo", PageRequest.of(0, 10))).thenReturn(mockPage);

        // Act
        var result = service.getUsersByName("foo", 10, 0);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should update an User")
    void updateUser() {
        // Arrange
        var mockUser = new User("Roger2", "roger2@email.com");
        mockUser.setId(3L);
        when(userRepository.findById(3L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // Act
        var result = service.updateUser(mockUser);
        var fetched = service.getUserById(mockUser.getId());

        // Assert
        assertEquals(mockUser, result);
        assertEquals(mockUser, fetched);
    }

    @Test
    @DisplayName("should delete an User")
    void deleteUser() {
        // Arrange
        var mockUser = new User("Test", "test@mail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // Act
        var result = service.deleteUser(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("should not delete an user that does not exist")
    public void deleteUser_ReturnFalse() {
        // Arrange
        when(userRepository.findById(101L)).thenReturn(Optional.empty());

        // Act
        var result = service.deleteUser(101L);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("should not allow to repeat email when creating users")
    void createUserFail() {
        // Arrange
        var newUser = new User("Test", "roger@email.com");
        when(userRepository.findByEmail("roger@email.com")).thenReturn(Optional.of(newUser));

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    service.createUser(newUser);
                });
    }
}
