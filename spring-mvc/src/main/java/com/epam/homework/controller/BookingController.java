package com.epam.homework.controller;

import com.epam.homework.facade.BookingFacade;
import com.epam.homework.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookingController {

    @Autowired
    BookingFacade bookingFacade;

    @RequestMapping("/health")
    @ResponseBody
    public String health() {
        return "Healthy";
    }

    @PostMapping("/tickets/book")
    public String bookTicket(@RequestParam String userId, @RequestParam String eventId, @RequestParam String place, @RequestParam String category) {
        int user = Integer.parseInt(userId);
        int event = Integer.parseInt(eventId);
        int placeInt = Integer.parseInt(place);
        Ticket.Category ticketCategory = Ticket.Category.valueOf(category);
        bookingFacade.bookTicket(user, event, placeInt, ticketCategory);
        return "redirect:/tickets?success";
    }
}
