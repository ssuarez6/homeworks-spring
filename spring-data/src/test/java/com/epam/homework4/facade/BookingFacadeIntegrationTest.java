package com.epam.homework4.facade;

import com.epam.homework4.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("BookingFacade")
public class BookingFacadeIntegrationTest {

    @Autowired
    private BookingFacadeImpl bookingFacade;

    private final Date today = Date.from(Instant.now());

    @Test
    @DisplayName("should run a ticket booking flow")
    public void bookTicketFlow() {

        // Arrange
        var user = new User();
        user.setName("Roger Federer");
        user.setEmail("roger@mail.com");
        user = bookingFacade.createUser(user);
        var userId = user.getId();

        var event = new Event();
        event.setTitle("Coldplay concert");
        event.setDate(today);
        event.setTicketPrice(10);
        event = bookingFacade.createEvent(event);
        var eventId = event.getId();

        var event2 = new Event();
        event2.setTitle("Magic show");
        event2.setDate(today);
        event2.setTicketPrice(25);
        event2 = bookingFacade.createEvent(event2);

        // Act
        var fetchedEvent = bookingFacade.getEventById(event.getId());
        var todaysEvents = bookingFacade.getEventsForDay(today, 10, 0);
        var firstEvent = todaysEvents.get(0);
        var secondEvent = todaysEvents.get(1);
        bookingFacade.depositMoney(user, 30);
        var createdTicket = bookingFacade.bookTicket(user.getId(), event.getId(), 1, Ticket.Category.PREMIUM);
        var userAccount = bookingFacade.createOrGetUserAccount(user);
        var fetchedTicket = bookingFacade.getBookedTickets(user, 10, 0);


        // Assert
        assertEquals(event.getTitle(), fetchedEvent.getTitle());
        assertEquals(2, todaysEvents.size());
        assertEquals(event.getId(), firstEvent.getId());
        assertEquals(event2.getId(), secondEvent.getId());
        assertNotNull(createdTicket);
        assertEquals(30 - event.getTicketPrice(), userAccount.getMoney());
        assertEquals(1, fetchedTicket.size());
        assertThrows(IllegalStateException.class, () -> {
            bookingFacade.bookTicket(userId, eventId, 1, Ticket.Category.PREMIUM);
        });
        var cancelTicketResult = bookingFacade.cancelTicket(createdTicket.getId());
        var fetchedAgain = bookingFacade.getBookedTickets(event, 10, 0);
        assertTrue(cancelTicketResult);
        assertTrue(fetchedAgain.isEmpty());
    }
}
