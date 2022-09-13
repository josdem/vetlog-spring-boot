package com.jos.dem.vetlog.validator;

import com.jos.dem.vetlog.command.PetCommand;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.validation.Errors;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Slf4j
class PetValidatorTest {

  private PetValidator validator = new PetValidator();

  @Test
  @DisplayName("validating birthdate")
  void shouldValidateBirthdate(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    Errors errors = mock(Errors.class);
    PetCommand petCommand = new PetCommand();
    petCommand.setBirthDate("2021-01-17T00:00");
    validator.validate(petCommand, errors);
    verify(errors, never()).rejectValue(anyString(), anyString());
  }
}
