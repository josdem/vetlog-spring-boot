package com.jos.dem.vetlog.validator;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.jos.dem.vetlog.command.PetCommand;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.validation.Errors;

@Slf4j
class PetValidatorTest {

    private PetValidator validator = new PetValidator();
    private Errors errors = mock(Errors.class);

    @Test
    @DisplayName("validating birthdate")
    void shouldValidateBirthdate(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        PetCommand petCommand = getPetCommand("2021-01-17T00:00");
        validator.validate(petCommand, errors);
        verify(errors, never()).rejectValue(anyString(), anyString());
    }

    @Test
    @DisplayName("rejecting a birthdate")
    void shouldRejectBirthdate(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        PetCommand petCommand = getPetCommand("2026-01-17T00:00");
        validator.validate(petCommand, errors);
        verify(errors).rejectValue("birthDate", "pet.error.birthDate.past");
    }

    @NotNull
    private PetCommand getPetCommand(String birthdate) {
        PetCommand petCommand = new PetCommand();
        petCommand.setBirthDate(birthdate);
        return petCommand;
    }
}
