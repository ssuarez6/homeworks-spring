package com.epam.homework4.service;

import com.epam.homework4.dao.TicketRepository;
import com.epam.homework4.model.Ticket;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private TicketRepository repo;

    private UserAccountService userAccountService;

    private EventService eventService;

    @Transactional
    public Ticket bookTicket(Long userId, Long eventId, int place, Ticket.Category category) {
        var bookedTicketOptional = repo.findBookedTicket(userId, eventId, place, category);

        if (bookedTicketOptional.isEmpty()) {

            var event = eventService.findById(eventId);
            if(event.isEmpty())
                throw new NullPointerException(String.format("Event with id [%d] does not exist", eventId));

            var userAccountOptional = userAccountService.findByUserId(userId);
            if(userAccountOptional.isEmpty())
                throw new NullPointerException(String.format("User with id [%s] does not have an account.", userId));

            var userAccount = userAccountOptional.get();
            var ticketPrice = event.get().getTicketPrice();

            if(event.get().getTicketPrice() > userAccountOptional.get().getMoney()) {
                throw new IllegalArgumentException("Insufficient funds");
            }

            userAccount.setMoney(userAccount.getMoney() - ticketPrice);
            userAccountService.update(userAccount);
            var newTicket = new Ticket(eventId, userId, category, place);
            return repo.save(newTicket);
        } else {
            logger.error(
                    "Cannot book ticket due to it being already booked {} {} {} {}",
                    eventId,
                    userId,
                    category,
                    place);
            throw new IllegalStateException("This place is already booked.");
        }
    }

    public List<Ticket> getBookedTicketsForUser(long userId, int pageSize, int pageNum) {
        logger.debug("About to find tickets for user {}", userId);
        var result = repo.findByUserId(userId, PageRequest.of(pageNum, pageSize));
        logger.info("Found {} tickets for user {}", result.getTotalElements(), userId);
        return result.stream().toList();
    }

    public List<Ticket> getBookedTicketsForEvent(long eventId, int pageSize, int pageNum) {
        logger.debug("About to find tickets for event {}", eventId);
        var result = repo.findByEventId(eventId, PageRequest.of(pageNum, pageSize));
        logger.info("Found {} tickets for event {}", result.getTotalElements(), eventId);
        return result.stream().toList();
    }

    public boolean cancelTicket(long ticketId) {
        if(repo.findById(ticketId).isEmpty()) return false;
        repo.deleteById(ticketId);
        return true;
    }

    public Optional<Ticket> findById(long ticketId) {
        return repo.findById(ticketId);
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setUserAccountService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Autowired
    public void setRepo(TicketRepository repo) {
        this.repo = repo;
    }

}
