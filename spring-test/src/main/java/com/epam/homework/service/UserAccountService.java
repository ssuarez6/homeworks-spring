package com.epam.homework.service;

import com.epam.homework.repository.UserAccountRepository;
import com.epam.homework.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private UserAccountRepository repo;

    @Autowired
    public void setRepo(UserAccountRepository repo) {
        this.repo = repo;
    }

    public Optional<UserAccount> findByUserId(long userId) {
        return repo.findByUserId(userId);
    }

    public UserAccount update(UserAccount userAccount) {
        var userAcc = repo.findById(userAccount.getId());
        if(userAcc.isEmpty()) throw new NullPointerException(String.format("UserAccount with id [%d] does not exist", userAccount.getId()));

        return (UserAccount) repo.save(userAccount);
    }

    public UserAccount save(UserAccount userAccount) {
        return repo.save(userAccount);
    }

    public List<UserAccount> getAll() {
        return repo.findAll();
    }
}
