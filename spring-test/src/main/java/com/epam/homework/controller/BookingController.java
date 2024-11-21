package com.epam.homework.controller;

import com.epam.homework.facade.BookingFacade;
import com.epam.homework.jms.JmsProducer;
import com.epam.homework.jms.TicketMessage;
import com.epam.homework.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookingController {

    @Autowired
    BookingFacade bookingFacade;

    @Autowired
    JmsProducer jmsProducer;

    public BookingController(BookingFacade bookingFacade, JmsProducer jmsProducer) {
        this.bookingFacade = bookingFacade;
        this.jmsProducer = jmsProducer;
    }

    @RequestMapping("/health")
    @ResponseBody
    public String health() {
        return "Healthy";
    }

    @PostMapping("/tickets/book")
    public ResponseEntity<?> bookTicket(@RequestParam String userId, @RequestParam String eventId, @RequestParam String place, @RequestParam String category) {
        int user = Integer.parseInt(userId);
        int event = Integer.parseInt(eventId);
        int placeInt = Integer.parseInt(place);
        Ticket.Category ticketCategory = Ticket.Category.valueOf(category);
        var ticket = bookingFacade.bookTicket(user, event, placeInt, ticketCategory);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/tickets/book/async")
    public ResponseEntity<?> bookTicketAsync(@RequestParam String userId, @RequestParam String eventId, @RequestParam String place, @RequestParam String category) {
        try {
            var ticketMessage = new TicketMessage(userId, eventId, place, category);
            jmsProducer.produceMessage(ticketMessage);
            return ResponseEntity.accepted().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
