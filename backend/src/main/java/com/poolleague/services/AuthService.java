package com.poolleague.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.poolleague.models.User;
import com.poolleague.utils.JwtUtil;
import com.poolleague.models.AuthResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthService(BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Transactional
    public AuthResponse registerUser(String firstName, String lastName, String email, String password, String phone) {
        logger.info("Starting user registration for email: {}", email);
        if (userService.findByEmail(email) != null) {
            throw new UserRegistrationException("Email already exists!");
        }
        
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(passwordEncoder.encode(password));
        
        try {
            logger.info("Attempting to create user with ID: {}", user.getId());
            user = userService.createUser(user);
            logger.info("User created successfully with ID: {}", user.getId());
        } catch (Exception e) {
            logger.error("Error during user registration: {}", e.getMessage(), e);
            throw new RuntimeException("User registration failed");
        }
        
        String token = jwtUtil.generateToken(email);
        return new AuthResponse(token, user);
    }

    public AuthResponse loginUser(String email, String password) {
        User user = userService.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            return new AuthResponse("Invalid email or password!");
        }
        
        String token = jwtUtil.generateToken(email);
        return new AuthResponse(token, user);
    }

    public User getUserFromToken(String token) {
        String email = jwtUtil.extractUsername(token);
        return userService.findByEmail(email);
    }
}
