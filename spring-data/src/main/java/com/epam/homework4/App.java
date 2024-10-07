package com.epam.homework4;

import com.epam.homework4.dao.EventRepository;
import com.epam.homework4.dao.TicketRepository;
import com.epam.homework4.dao.UserAccountRepository;
import com.epam.homework4.dao.UserRepository;
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
        System.out.println("Database initialized from .sql files.");

        System.out.println("-----------------------------------");
        System.out.println("Users:");
        System.out.println("-----------------------------------");
        userRepository
                .findAll()
                .forEach(
                        user ->
                                System.out.printf(
                                        "%s: %s - %s\n",
                                        user.getId(), user.getName(), user.getEmail()));

        System.out.println("-----------------------------------");
        System.out.println("Events:");
        System.out.println("-----------------------------------");
        eventRepository
                .findAll()
                .forEach(
                        event ->
                                System.out.printf(
                                        "%s: %s on %s - Price: %f\n",
                                        event.getId(),
                                        event.getTitle(),
                                        event.getDate().toString(),
                                        event.getTicketPrice()));

        System.out.println("-----------------------------------");
        System.out.println("Tickets:");
        System.out.println("-----------------------------------");
        ticketRepository
                .findAll()
                .forEach(
                        ticket ->
                                System.out.printf(
                                        "Ticket ID: %s - Event: %s - User: %s - Category: %s - Place: %d\n",
                                        ticket.getId(),
                                        ticket.getEventId(),
                                        ticket.getUserId(),
                                        ticket.getCategory(),
                                        ticket.getPlace()));

        System.out.println("-----------------------------------");
        System.out.println("UserAccounts:");
        System.out.println("-----------------------------------");
        userAccountRepository
                .findAll()
                .forEach(
                        userAccount ->
                                System.out.printf(
                                        "UserAccount ID: %s - UserId: %s - Money: %f\n",
                                        userAccount.getId(),
                                        userAccount.getUserId(),
                                        userAccount.getMoney()));
    }
}
