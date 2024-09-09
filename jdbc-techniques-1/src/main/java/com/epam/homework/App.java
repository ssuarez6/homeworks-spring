package com.epam.homework;

import com.epam.homework.config.AppConfig;
import com.epam.homework.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        var repo = context.getBean(UserRepository.class);
        var minFriends = 100;
        var minLikes = 20;
        var users = repo.findUsersWithMoreThanFriendsAndLikes();
        System.out.printf("Users with more than %d friends and more than %d likes in March 2025:\n", minFriends, minLikes);
        users.forEach(user -> {
            System.out.printf("%s %s\n", user.getName(), user.getSurname());
        });

    }
}
