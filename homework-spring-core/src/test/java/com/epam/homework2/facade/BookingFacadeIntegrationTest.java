package com.epam.homework2.facade;

import com.epam.homework2.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.time.Instant;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BookingFacade")
public class BookingFacadeIntegrationTest {

    private ApplicationContext context;
    private BookingFacade bookingFacade;

    @BeforeEach
    public void beforeEach() {
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        bookingFacade = context.getBean(BookingFacade.class);
    }

    @Test
    @DisplayName("should run a ticket booking flow")
    public void bookTicketFlow() {
        var user = new User(10L, "Roger Federer", "roger@mail.com");
        bookingFacade.createUser(user);

        var event = new Event(10L, "Coldplay concert", Date.from(Instant.now()));
        var event2 = new Event(20L, "Magic show", Date.from(Instant.now()));
        bookingFacade.createEvent(event);
        bookingFacade.createEvent(event2);

        var fetchedEvent = bookingFacade.getEventById(event.id());
        assertEquals(fetchedEvent.title(), event.title());

        var todaysEvents = bookingFacade.getEventsForDay(Date.from(Instant.now()), 10, 1);
        assertEquals(2, todaysEvents.size());
        assertEquals(Set.of(event, event2), Set.copyOf(todaysEvents));

        var createdTicket = bookingFacade.bookTicket(10L, 10L, 1, Ticket.Category.PREMIUM);
        assertNotNull(createdTicket);

        var fetchedTicket = bookingFacade.getBookedTickets(user, 10, 1);
        assertEquals(1, fetchedTicket.size());

        assertThrows(IllegalStateException.class, () -> {
            bookingFacade.bookTicket(10L, 10L, 1, Ticket.Category.PREMIUM);
        });

        assertTrue(bookingFacade.cancelTicket(createdTicket.id()));

        var fetchedAgain = bookingFacade.getBookedTickets(event, 10, 1);
        assertTrue(fetchedAgain.isEmpty());
    }
}
