package com.epam.homework2.service;

import com.epam.homework2.dao.InMemoryStorage;
import com.epam.homework2.dao.TicketDao;
import com.epam.homework2.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {

    private Map<Long, Ticket> tickets = new HashMap<>();

    private InMemoryStorage storage = new InMemoryStorage();

    private TicketDao dao;

    private TicketService service = new TicketService();

    @BeforeEach
    public void beforeEach() {
        tickets.clear();
        Ticket t1 = new Ticket(1, 1, 1, Ticket.Category.BAR, 1);
        Ticket t2 = new Ticket(2, 1, 2, Ticket.Category.STANDARD, 2);
        Ticket t3 = new Ticket(3, 2, 3, Ticket.Category.PREMIUM, 1);
        Ticket t4 = new Ticket(4, 1, 1, Ticket.Category.BAR, 2);
        tickets.put(t1.id(), t1);
        tickets.put(t2.id(), t2);
        tickets.put(t3.id(), t3);
        tickets.put(t4.id(), t4);
        storage.initialize(Map.of(), Map.of(), tickets);
        dao = new TicketDao();
        dao.setTickets(storage);
        service.setDao(dao);
    }

    @Test
    @DisplayName("Should book a ticket")
    void bookTicket() {
        var result = service.bookTicket(1L, 4L, 1, Ticket.Category.PREMIUM);
        assertNotNull(result);
        var fetched = service.findById(result.id());
        assertEquals(result, fetched);
    }

    @Test
    @DisplayName("should fail booking an already booked ticket")
    void notBookTicket() {
        assertThrows(IllegalStateException.class, () -> {
            service.bookTicket(1L, 1L, 1, Ticket.Category.BAR);
        });
    }

    @Test
    @DisplayName("should get all tickets for a user")
    void getBookedTicketsForUser() {
        var result = service.getBookedTicketsForUser(1, 5, 1);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("should get all tickets for an event with pagination")
    void getBookedTicketsForEvent() {
        var result = service.getBookedTicketsForEvent(1, 2, 1);
        assertEquals(2, result.size());
        var result2 = service.getBookedTicketsForEvent(1, 2, 2);
        assertEquals(1, result2.size());
    }

    @Test
    @DisplayName("should cancel a ticket")
    void cancelTicket() {
        assertTrue(service.cancelTicket(1));
        assertFalse(service.cancelTicket(99));
    }
}