package com.epam.homework2.service;

import com.epam.homework2.dao.InMemoryStorage;
import com.epam.homework2.dao.UserDao;
import com.epam.homework2.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserService")
class UserServiceTest {

    private Map<Long, User> users = new HashMap<>();

    private InMemoryStorage storage = new InMemoryStorage();

    private UserDao dao;

    private UserService service = new UserService();

    @BeforeEach
    public void beforeEach() {
        users.clear();
        User a = new User(1, "Roger", "roger@email.com");
        User b = new User(2, "Rafael", "rafa@email.com");
        User c = new User(3, "Santiago", "santi@email.com");
        User d = new User(4, "Novak", "novak@email.com");
        User e = new User(5, "Santiago", "santi2@email.com");
        users.put(a.id(), a);
        users.put(b.id(), b);
        users.put(c.id(), c);
        users.put(d.id(), d);
        users.put(e.id(), e);
        storage.initialize(users, Map.of(), Map.of());
        dao = new UserDao();
        dao.setUsers(storage);
        service.setDao(dao);
    }

    @Test
    @DisplayName("should get an User by id")
    void getUserById() {
        var user = service.getUserById(1L);
        assertEquals(user.name(), "Roger");

        assertNull(service.getUserById(100L));
    }

    @Test
    @DisplayName("should get an User by its email")
    void getUserByEmail() {
        var user = service.getUserByEmail("rafa@email.com");
        assertEquals(2L, user.id());
        assertNull(service.getUserByEmail("inexistent-mail@mail.com"));
    }

    @Test
    @DisplayName("should get all Users by a name")
    void getUsersByName() {
        var users = service.getUsersByName("Santiago", 3, 1);
        assertEquals(2, users.size());
        var pageSizeOne = service.getUsersByName("Santiago", 1, 1);
        assertEquals(1, pageSizeOne.size());
        var empty = service.getUsersByName("Non-present-name", 10, 1);
        assertTrue(empty.isEmpty());
    }

    @Test
    @DisplayName("should update an User")
    void updateUser() {
        var modifiedUser = new User(1, "Roger2", "roger2@email.com");
        var result = service.updateUser(modifiedUser);
        assertEquals(modifiedUser, result);
        var fetched = service.getUserById(modifiedUser.id());
        assertEquals(modifiedUser, fetched);

        var badUser = new User(100, "name", "mail");
        assertThrows(IllegalArgumentException.class, () -> {
            service.updateUser(badUser);
        });
    }

    @Test
    @DisplayName("should delete an User")
    void deleteUser() {
        assertTrue(service.deleteUser(1L));
        assertFalse(service.deleteUser(101L));
    }

    @Test
    @DisplayName("should not allow to repeat email when creating useres")
    void createUserFail() {
        var newUser = new User(6, "Test", "roger@email.com");
        assertThrows(IllegalArgumentException.class, () -> {
           service.createUser(newUser);
        });
    }
}