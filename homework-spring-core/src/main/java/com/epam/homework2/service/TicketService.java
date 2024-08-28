package com.epam.homework2.service;

import com.epam.homework2.dao.TicketDao;
import com.epam.homework2.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private TicketDao dao;

    @Autowired
    public void setDao(TicketDao dao) {
        this.dao = dao;
    }

    public Ticket bookTicket(Long userId, Long eventId, int place, Ticket.Category category) {
        var ticketsForEvent = dao.getFilteredTickets(ticket -> ticket.eventId() == eventId &&
                ticket.category() == category &&
                ticket.place() == place, 1, 1);

        if (!ticketsForEvent.isEmpty()) { // already booked
            logger.error("Cannot book ticket due to it being already booked {} {} {} {}", eventId, userId, category, place);
            throw new IllegalStateException("This place is already booked.");
        } else {
            var randomId = new Random().nextLong();
            var newTicket = new Ticket(randomId, eventId, userId, category, place);
            logger.debug("Generating new ticket with id {}", randomId);
            dao.save(newTicket);
            return newTicket;
        }
    }

    public List<Ticket> getBookedTicketsForUser(long userId, int pageSize, int pageNum) {
        logger.debug("About to find tickets for user {}", userId);
        var result = dao.getFilteredTickets(ticket -> ticket.userId() == userId, pageSize, pageNum);
        logger.info("Found {} tickets for user {}", result.size(), userId);
        return result;
    }

    public List<Ticket> getBookedTicketsForEvent(long eventId, int pageSize, int pageNum){
        logger.debug("About to find tickets for event {}", eventId);
        var result = dao.getFilteredTickets(ticket -> ticket.eventId() == eventId, pageSize, pageNum);
        logger.info("Found {} tickets for event {}", result.size(), eventId);
        return result;
    }

    public boolean cancelTicket(long ticketId) {
        return dao.delete(ticketId);
    }

    public Ticket findById(long ticketId) {
        return dao.findById(ticketId);
    }
}
