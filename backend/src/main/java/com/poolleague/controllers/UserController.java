package com.poolleague.controllers;

import com.poolleague.models.User;
import com.poolleague.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUserProfile(@RequestBody User user) {
        User updatedUser = userService.updateUserProfile(user);
        return ResponseEntity.ok(updatedUser);
    }
}
