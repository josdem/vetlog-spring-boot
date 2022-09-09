package com.jos.dem.vetlog.validator;

import com.jos.dem.vetlog.command.ChangePasswordCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.validation.Errors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Slf4j
class ChangePasswordValidatorTest {

  private ChangePasswordValidator validator = new ChangePasswordValidator();

  @Test
  @DisplayName("not validating since password does not match")
  void shouldRejectSincePasswordsAreNotEquals(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    ChangePasswordCommand command = new ChangePasswordCommand();
    command.setToken("token");
    command.setPassword("password");
    command.setPasswordConfirmation("passwords");
    Errors errors = mock(Errors.class);
    validator.validate(command, errors);
    verify(errors).rejectValue("password", "user.error.password.equals");
  }
}
