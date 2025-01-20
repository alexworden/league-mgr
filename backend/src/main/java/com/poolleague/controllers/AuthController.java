package com.poolleague.controllers;

import com.poolleague.services.AuthService;
import com.poolleague.models.User;
import com.poolleague.models.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody UserSignupRequest request) {
        try {
            logger.info("Received signup request for email: {}", request.email);
            AuthResponse response = authService.registerUser(
                request.firstName,
                request.lastName,
                request.email,
                request.password,
                request.phone
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during signup: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLoginRequest request) {
        try {
            AuthResponse response = authService.loginUser(request.email, request.password);
            if (response.isAuthenticated()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(response);
            }
        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        try {
            User user = authService.getUserFromToken(token.replace("Bearer ", ""));
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error getting current user: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}

class UserSignupRequest {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String phone;
}

class UserLoginRequest {
    public String email;
    public String password;
}
