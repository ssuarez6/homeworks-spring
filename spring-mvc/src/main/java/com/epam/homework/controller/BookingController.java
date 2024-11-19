package com.epam.homework.controller;

import com.epam.homework.facade.BookingFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BookingController {

    @Autowired
    BookingFacade bookingFacade;

    @RequestMapping("/health")
    @ResponseBody
    public String health() {
        return "Healthy";
    }

    @GetMapping("/event/{id}")
    public String getEventById(@PathVariable long id, Model model) {
        var event = bookingFacade.getEventById(id);
        model.addAttribute("event", event);
        return "event";
    }
}
