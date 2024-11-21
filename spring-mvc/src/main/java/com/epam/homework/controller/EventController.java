package com.epam.homework.controller;

import com.epam.homework.facade.BookingFacade;
import com.epam.homework.model.Event;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Controller
public class EventController {
    @Autowired
    private final BookingFacade bookingFacade;

    public EventController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping("/events")
    public String getEvents(Model model) {
        List<Event> events = bookingFacade.getAllEvents();
        model.addAttribute("event", new Event());
        model.addAttribute("events", events);
        return "events";
    }

    @PostMapping("/events")
    public String createEvent(HttpServletRequest request) throws ParseException {
        String title = request.getParameter("title");
        String date = request.getParameter("date");
        String price = request.getParameter("ticketPrice");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        Event event = new Event(title, formatter.parse(date), Double.parseDouble(price));
        bookingFacade.createEvent(event);
        return "redirect:/events?success";
    }
}
