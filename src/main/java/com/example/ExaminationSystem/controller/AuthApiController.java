package com.example.ExaminationSystem.controller;

import com.example.ExaminationSystem.model.User;
import com.example.ExaminationSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registration successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed. User may already exist.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.ok("Login successful. Role: " + user.getRole());
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
