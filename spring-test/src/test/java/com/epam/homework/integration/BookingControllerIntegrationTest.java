package com.epam.homework.integration;

import com.epam.homework.controller.BookingController;
import com.epam.homework.facade.BookingFacade;
import com.epam.homework.jms.JmsConsumer;
import com.epam.homework.jms.JmsProducer;
import com.epam.homework.model.Event;
import com.epam.homework.model.Ticket;
import com.epam.homework.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(AppTestConfig.class)
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JmsTemplate template;

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private BookingController bookingController;

    @Autowired
    private JmsProducer jmsProducer;

    @Autowired
    private JmsConsumer jmsConsumer;

    @Test
    public void bookTicketsAsync() throws Exception {
        var user = new User("user", "user@mail.com");
        var event = new Event("Circus", Date.from(Instant.now()), 12.0D);
        var createdUser = bookingFacade.createUser(user);
        var createdEvent = bookingFacade.createEvent(event);
        var expectedPlace = 1;
        var expectedCategory = Ticket.Category.PREMIUM;

        mockMvc.perform(post("/tickets/book/async")
                .param("userId", String.valueOf(createdUser.getId()))
                .param("eventId", String.valueOf(createdEvent.getId()))
                .param("place", String.valueOf(expectedPlace))
                .param("category", expectedCategory.name()))
                .andExpect(status().isAccepted());

        Thread.sleep(3000);

        List<Ticket> tickets = bookingFacade.getBookedTickets(createdUser, 10, 1);
        assertEquals(1, tickets.size());

        var ticket = tickets.get(0);
        assertEquals(createdEvent.getId(), ticket.getEventId());
        assertEquals(createdUser.getId(), ticket.getUserId());
        assertEquals(expectedPlace, ticket.getPlace());
        assertEquals(expectedCategory, ticket.getCategory());
    }
}
