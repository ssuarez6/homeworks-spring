package com.epam.homework2.dao;

import com.epam.homework2.model.Event;
import com.epam.homework2.model.Ticket;
import com.epam.homework2.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryStorage {

    private final Map<Long, User> usersMap = new HashMap<>();
    private final Map<Long, Event> eventsMap = new HashMap<>();
    private final Map<Long, Ticket> ticketsMap = new HashMap<>();

    public Map<Long, User> getUsersMap() {
        return usersMap;
    }

    public Map<Long, Event> getEventsMap() {
        return eventsMap;
    }

    public Map<Long, Ticket> getTicketsMap() {
        return ticketsMap;
    }

    public void initialize(Map<Long, User> users, Map<Long, Event> events, Map<Long, Ticket> tickets) {
        usersMap.putAll(users);
        eventsMap.putAll(events);
        ticketsMap.putAll(tickets);
    }
}
