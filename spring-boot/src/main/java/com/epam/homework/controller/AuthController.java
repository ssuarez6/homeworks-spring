package com.epam.homework.controller;

import com.epam.homework.dto.LoginRequest;
import com.epam.homework.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUser(), loginRequest.getPassword()));
            if("user".equals(loginRequest.getUser()) && "password".equals(loginRequest.getPassword())) {
                return ResponseEntity.ok(JwtTokenUtil.generateToken(loginRequest.getUser(), "USER"));
            } else if("admin".equals(loginRequest.getUser()) && "password".equals(loginRequest.getPassword())) {
                return ResponseEntity.ok(JwtTokenUtil.generateToken(loginRequest.getUser(), "ADMIN"));
            } else {
                throw new RuntimeException("Invalid Credentials");
            }
        } catch (AuthenticationException ae) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }

    }
}
