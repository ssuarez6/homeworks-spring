package com.epam.homework.jms;

import com.epam.homework.facade.BookingFacade;
import com.epam.homework.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class JmsConsumer {

    private static final Logger logger = LoggerFactory.getLogger(JmsConsumer.class);

    @Autowired
    private BookingFacade bookingFacade;

    public JmsConsumer(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @JmsListener(destination = "${spring.tickets.queue}")
    public void processMessage(TicketMessage message) {
        try {
            var ticket = bookingFacade.bookTicket(Long.parseLong(message.getUserId()), Long.parseLong(message.getEventId()), Integer.parseInt(message.getPlace()), Ticket.Category.valueOf(message.getCategory()));
            logger.info("Successfully booked ticket with ID: " + ticket.getId());
        } catch (Exception ex) {
            logger.error("There was an error trying to book the ticket", ex);
        }
    }
}
