package com.jos.dem.vetlog.binder;

import com.jos.dem.vetlog.command.UserCommand;
import com.jos.dem.vetlog.enums.Role;
import com.jos.dem.vetlog.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class UserBinderTest {

  private UserBinder binder = new UserBinder();

  @Test
  @DisplayName("binding an user")
  void shouldBindAnUser(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    UserCommand userCommand = new UserCommand();
    userCommand.setUsername("josdem");
    userCommand.setPassword("password");
    userCommand.setPasswordConfirmation("password");
    userCommand.setFirstname("Jose");
    userCommand.setLastname("Morales");
    userCommand.setEmail("contact@josdem.io");

    User result = binder.bindUser(userCommand);

    assertEquals("josdem", result.getUsername());
    assertEquals(60, result.getPassword().length());
    assertEquals("Jose", result.getFirstname());
    assertEquals("Morales", result.getLastname());
    assertEquals("contact@josdem.io", result.getEmail());
    assertEquals(Role.USER, result.getRole());
  }
}
