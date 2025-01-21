package com.poolleague.services;

import com.poolleague.models.AuthResponse;
import com.poolleague.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        deleteTestUser("john.doe@example.com");
        deleteTestUser("jane.doe@example.com");
    }

    @Test
    public void testRegisterUser() {
        AuthResponse response = authService.registerUser("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        assertNotNull(response, "Response should not be null");
        assertTrue(response.getToken() != null, "Token should not be null");
    }

    @Test
    public void testIsolatedUserRegistration() {
        User user = createTestUser("Jane", "Doe", "jane.doe@example.com", "password456", "0987654321");

        // Verify that the user was created successfully
        User createdUser = userService.findByEmail(user.getEmail());
        assertNotNull(createdUser, "User should be created successfully");
    }

    @Test
    public void testLoginUser() {
        createTestUser("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        AuthResponse response = authService.loginUser("john.doe@example.com", "password123");
        assertNotNull(response, "Response should not be null");
        assertTrue(response.getToken() != null, "Token should not be null");
    }

    @Test
    public void testGetUserFromToken() {
        createTestUser("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        String token = authService.loginUser("john.doe@example.com", "password123").getToken();
        User user = authService.getUserFromToken(token);
        assertNotNull(user, "User should not be null");
        assertTrue(user.getEmail().equals("john.doe@example.com"), "User email should match");
    }

    @Test
    public void testLoginWithIncorrectPassword() {
        createTestUser("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        AuthResponse response = authService.loginUser("john.doe@example.com", "wrongpassword");
        assertTrue(response.getToken() == null, "Token should be null for incorrect password");
    }

    @Test
    public void testLoginWithNonExistentUsername() {
        createTestUser("John", "Doe", "john.doe@example.com", "password123", "1234567890");
        AuthResponse response = authService.loginUser("nonexistent@example.com", "password123");
        assertTrue(response.getToken() == null, "Token should be null for non-existent username");
    }

    private User createTestUser(String firstName, String lastName, String email, String password, String phone) {
        return authService.registerUser(firstName, lastName, email, password, phone).getUser();
    }

    public void deleteTestUser(String email) {
        try {
            User existingUser = userService.findByEmail(email);
            if (existingUser != null) {
                // Clean up if needed
                userService.deleteUser(existingUser.getId());
            }
        } catch (Exception e) {
            // User doesn't exist, which is fine for tests
        }
    }
}
