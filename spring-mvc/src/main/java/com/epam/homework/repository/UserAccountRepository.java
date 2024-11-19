package com.epam.homework.repository;

import com.epam.homework.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByUserId(long userId);
}
