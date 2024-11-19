package com.epam.homework;

import com.epam.homework.repository.EventRepository;
import com.epam.homework.repository.TicketRepository;
import com.epam.homework.repository.UserAccountRepository;
import com.epam.homework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {
    @Autowired private UserRepository userRepository;

    @Autowired private EventRepository eventRepository;

    @Autowired private TicketRepository ticketRepository;

    @Autowired private UserAccountRepository userAccountRepository;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) {
    }
}
