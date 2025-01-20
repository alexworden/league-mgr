package com.poolleague.controllers;

import com.poolleague.services.AuthService;
import com.poolleague.models.User;
import com.poolleague.models.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequest request) {
        AuthResponse response = authService.registerUser(
            request.firstName,
            request.lastName,
            request.email,
            request.password,
            request.phone
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        AuthResponse response = authService.loginUser(request.email, request.password);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        User user = authService.getUserFromToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(user);
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
