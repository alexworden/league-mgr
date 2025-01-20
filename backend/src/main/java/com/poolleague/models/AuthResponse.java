package com.poolleague.models;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.userId = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }
}
