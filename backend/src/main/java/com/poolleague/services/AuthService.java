package com.poolleague.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.poolleague.repositories.UserRepository;
import com.poolleague.models.User;
import com.poolleague.utils.JwtUtil;
import com.poolleague.models.AuthResponse;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse registerUser(String firstName, String lastName, String email, String password, String phone) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(passwordEncoder.encode(password));
        user = userRepository.save(user);
        
        String token = jwtUtil.generateToken(email);
        return new AuthResponse(token, user);
    }

    public AuthResponse loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid email or password!");
        }
        
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password!");
        }
        
        String token = jwtUtil.generateToken(email);
        return new AuthResponse(token, user);
    }

    public User getUserFromToken(String token) {
        String email = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
