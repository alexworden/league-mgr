package com.poolleague.services;

import com.poolleague.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

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
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService.deleteAllUsers();
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedpassword");
        user.setFirstName("Test");
        user.setLastName("User");

        User createdUser = userService.createUser(user);

        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertEquals("test@example.com", createdUser.getEmail());

        User fetchedUser = userService.findById(createdUser.getId());
        Assertions.assertEquals("Test", fetchedUser.getFirstName());
    }

    @Test
    public void testUpdateUserProfile() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash("hashedpassword");
        user.setFirstName("Test");
        user.setLastName("User");

        User createdUser = userService.createUser(user);
        createdUser.setFirstName("Updated");

        User updatedUser = userService.updateUserProfile(createdUser);

        Assertions.assertEquals("Updated", updatedUser.getFirstName());
        
        User fetchedUser = userService.findById(updatedUser.getId());
        Assertions.assertEquals("Updated", fetchedUser.getFirstName());
    }

    @Test
    public void testCreateUserWithNullValues() {
        User user = new User();
        user.setEmail(null);
        user.setPasswordHash(null);
        user.setFirstName(null);
        user.setLastName(null);

        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });
    }

    @Test
    public void testUpdateNonExistentUser() {
        User user = new User();
        user.setId("non-existent-id");
        user.setFirstName("New Name");

        Assertions.assertThrows(RuntimeException.class, () -> {
            userService.updateUserProfile(user);
        });
    }
}
