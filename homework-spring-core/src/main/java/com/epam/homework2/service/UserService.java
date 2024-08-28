package com.epam.homework2.service;

import com.epam.homework2.dao.UserDao;
import com.epam.homework2.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    private UserDao dao;

    @Autowired
    public void setDao(UserDao dao) {
        this.dao = dao;
    }

    public User getUserById(Long id) {
        return dao.findById(id);
    }

    public User getUserByEmail(String email) {
        return dao.findByEmail(email);
    }

    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return dao.getFilteredUsers(user -> user.name().equals(name), pageSize, pageNum);
    }

    public User createUser(User user) {
        var emailUser = getUserByEmail(user.email());
        if(emailUser != null) {
            logger.error("Could not create user {} because the email was already registered", user.name());
            throw new IllegalArgumentException("Email " + user.email() + " is already in use");
        } else {
            logger.debug("Successfully created new User with id {}", user.id());
            dao.save(user);
            return user;
        }
    }

    public User updateUser(User user) {
        if(getUserById(user.id()) == null) {
            logger.error("Could not update non-existent user with id {}", user.id());
            throw new IllegalArgumentException("User with id " + user.id() + " does not exist");
        }
        dao.save(user);
        return user;
    }

    public boolean deleteUser(Long id) {
        return dao.deleteUser(id);
    }
}
