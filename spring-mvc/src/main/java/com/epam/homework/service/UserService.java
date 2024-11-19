package com.epam.homework.service;

import com.epam.homework.repository.UserRepository;
import com.epam.homework.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private UserRepository repo;

    @Autowired
    public void setRepo(UserRepository repo) {
        this.repo = repo;
    }

    public User getUserById(Long id) {
        var result = repo.findById(id);
        if (result.isEmpty())
            throw new NullPointerException(
                    String.format("User with id [%d] does not exist.\n", id));
        return result.get();
    }

    public User getUserByEmail(String email) {
        var result = repo.findByEmail(email);
        if (result.isEmpty())
            throw new NullPointerException(
                    String.format("User with email [%s] does not exist.\n", email));
        return result.get();
    }

    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        var result = repo.findByName(name, PageRequest.of(pageNum, pageSize));
        logger.debug("Found {} users with name {}", result.getTotalElements(), name);
        return result.stream().toList();
    }

    public User createUser(User user) {
        var emailUserOptional = repo.findByEmail(user.getEmail());
        if (emailUserOptional.isEmpty()) {
            var created = repo.save(user);
            logger.debug("Successfully created new User with id {}", user.getId());
            return created;
        } else {
            logger.error(
                    "Could not create user {} because the email was already registered",
                    user.getName());
            throw new IllegalArgumentException("Email " + user.getEmail() + " is already in use");
        }
    }

    public User updateUser(User user) {
        var userOptional = repo.findById(user.getId());
        if (userOptional.isEmpty()) {
            logger.error("Could not update non-existent user with id {}", user.getId());
            throw new IllegalArgumentException("User with id " + user.getId() + " does not exist");
        }
        return repo.save(user);
    }

    public boolean deleteUser(Long id) {
        if (repo.findById(id).isEmpty()) return false;
        else {
            repo.deleteById(id);
            return true;
        }
    }
}
