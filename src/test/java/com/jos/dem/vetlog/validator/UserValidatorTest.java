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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Slf4j
class UserValidatorTest {

  private UserValidator validator;
  @Mock private UserService userService;

  @BeforeEach
  void setup(){
    MockitoAnnotations.openMocks(this);
    validator = new UserValidator(userService);
  }

  @Test
  @DisplayName("rejecting an user since passwords do not match")
  void shouldRejectUserSincePasswordDoesNotMatch(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    UserCommand userCommand = new UserCommand();
    userCommand.setUsername("josdem");
    userCommand.setPassword("password");
    userCommand.setPasswordConfirmation("passwords");
    userCommand.setFirstname("Jose");
    userCommand.setLastname("Morales");
    userCommand.setEmail("contact@josdem.io");
    Errors errors = mock(Errors.class);
    validator.validate(userCommand, errors);
    verify(errors).rejectValue("password", "user.error.password.equals");
  }
}
