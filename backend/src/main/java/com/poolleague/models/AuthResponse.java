package com.poolleague.models;

public class AuthResponse {
    private boolean authenticated = false;
    private String token;
    private User user;
    private String errorMessage;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
        this.authenticated = true;
    }

    public AuthResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
