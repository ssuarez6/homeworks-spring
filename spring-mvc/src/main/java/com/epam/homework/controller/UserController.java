package com.epam.homework.controller;

import com.epam.homework.facade.BookingFacade;
import com.epam.homework.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {
    private final BookingFacade bookingFacade;

    public UserController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        var users = bookingFacade.getAllUserAccounts();
        model.addAttribute("user", new User());
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/user")
    public String createUser(HttpServletRequest request) {
        var name = request.getParameter("name");
        var email = request.getParameter("email");
        var user = new User(name, email);
        bookingFacade.createUser(user);
        return "redirect:users?success";
    }

    @PostMapping("/accounts/deposit")
    public String depositMoneyToAccount(@RequestParam String userId, @RequestParam double amount) {
        var user = bookingFacade.getUserById(Long.parseLong(userId));
        bookingFacade.depositMoney(user, amount);
        return "redirect:users?success";
    }
}
