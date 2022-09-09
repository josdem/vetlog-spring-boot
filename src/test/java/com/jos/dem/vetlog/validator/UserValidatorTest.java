package com.jos.dem.vetlog.validator;

import com.jos.dem.vetlog.command.UserCommand;
import com.jos.dem.vetlog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.Errors;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Slf4j
class UserValidatorTest {

  private UserValidator validator;
  private Errors errors = mock(Errors.class);

  @Mock private UserService userService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    validator = new UserValidator(userService);
  }

  @Test
  @DisplayName("validating an user")
  void shouldValidateAnUser(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    UserCommand userCommand = getUserCommand();
    validator.validate(userCommand, errors);
    verify(errors, never()).rejectValue(anyString(), anyString());
  }

  @Test
  @DisplayName("rejecting an user since passwords do not match")
  void shouldRejectUserSincePasswordDoesNotMatch(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    UserCommand userCommand = getUserCommand();
    userCommand.setPasswordConfirmation("passwords");
    validator.validate(userCommand, errors);
    verify(errors).rejectValue("password", "user.error.password.equals");
  }

  @Test
  @DisplayName("accepting dash in password")
  void shouldAcceptDashCharacterInPassword(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    UserCommand userCommand = getUserCommand();
    userCommand.setPassword("pass-word");
    userCommand.setPasswordConfirmation("pass-word");
    verify(errors, never()).rejectValue(anyString(), anyString());
  }

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
