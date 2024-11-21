package com.epam.homework.facade;

import com.epam.homework.model.*;
import com.epam.homework.service.EventService;
import com.epam.homework.service.TicketService;
import com.epam.homework.service.UserAccountService;
import com.epam.homework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingFacadeImpl implements BookingFacade {

    private EventService eventService;
    private TicketService ticketService;
    private UserService userService;
    private UserAccountService userAccountService;

    @Autowired
    public BookingFacadeImpl(EventService eventService, TicketService ticketService, UserService userService, UserAccountService userAccountService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.userAccountService = userAccountService;
    }

    @Override
    public Event getEventById(long id) {
        var event = eventService.findById(id);
        if(event.isEmpty()) throw new NullPointerException("Event does not exist.");
        return event.get();
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long id) {
        return userService.getUserById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public List<User> getAllUsers()  {
        return userService.getAllUsers();
    }

    @Override
    public List<UserAccount> getAllUserAccounts()  {
        return userAccountService.getAll();
    }

    @Override
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userService.deleteUser(userId);
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketService.getBookedTicketsForUser(user.getId(), pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return ticketService.getBookedTicketsForEvent(event.getId(), pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketService.cancelTicket(ticketId);
    }

    @Override
    public UserAccount createOrGetUserAccount(User user) {
        var account = userAccountService.findByUserId(user.getId());
        if(account.isEmpty()) {
            var newAccount = new UserAccount(user, 0);
            return userAccountService.save(newAccount);
        } else return account.get();
    }

    @Override
    public void depositMoney(User user, double amount) {
        if(amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        var userAccount = createOrGetUserAccount(user);
        userAccount.setMoney(userAccount.getMoney() + amount);
        userAccountService.save(userAccount);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventService.getAll();
    }

    @Override
    public void loadTickets(List<Ticket> tickets) {
        tickets.forEach(ticket -> {
            bookTicket(ticket.getUserId(), ticket.getEventId(), ticket.getPlace(), ticket.getCategory());
        });
    }
}
