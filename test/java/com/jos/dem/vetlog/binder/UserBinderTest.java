package com.jos.dem.vetlog.binder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jos.dem.vetlog.command.UserCommand;
import com.jos.dem.vetlog.enums.Role;
import com.jos.dem.vetlog.model.User;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@Slf4j
class UserBinderTest {

    private UserBinder binder = new UserBinder();

    @Test
    @DisplayName("binding an user")
    void shouldBindAnUser(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        UserCommand userCommand = getUserCommand();

        User result = binder.bindUser(userCommand);

        assertEquals("josdem", result.getUsername());
        assertEquals(60, result.getPassword().length());
        assertEquals("Jose", result.getFirstName());
        assertEquals("Morales", result.getLastName());
        assertEquals("contact@josdem.io", result.getEmail());
        assertEquals(Role.USER, result.getRole());
    }

    @NotNull
    private UserCommand getUserCommand() {
        UserCommand userCommand = new UserCommand();
        userCommand.setUsername("josdem");
        userCommand.setPassword("password");
        userCommand.setPasswordConfirmation("password");
        userCommand.setFirstname("Jose");
        userCommand.setLastname("Morales");
        userCommand.setEmail("contact@josdem.io");
        return userCommand;
    }
}
