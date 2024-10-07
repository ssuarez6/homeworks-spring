package com.epam.homework4.service;

import com.epam.homework4.dao.UserAccountRepository;
import com.epam.homework4.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
