package com.epam.homework.controller;

import com.epam.homework.facade.BookingFacade;
import com.epam.homework.model.User;
import com.epam.homework.model.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class UserController {
    private final BookingFacade bookingFacade;

    public UserController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserAccount>> getUsers(Model model) {
        var users = bookingFacade.getAllUserAccounts();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(HttpServletRequest request) {
        var name = request.getParameter("name");
        var email = request.getParameter("email");
        var user = new User(name, email);
        var newUser = bookingFacade.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/accounts/deposit")
    public ResponseEntity<?> depositMoneyToAccount(@RequestParam String userId, @RequestParam double amount) {
        var user = bookingFacade.getUserById(Long.parseLong(userId));
        bookingFacade.depositMoney(user, amount);
        return ResponseEntity.ok().build();
    }
}
