package com.josdem.vetlog.validator

import com.josdem.vetlog.command.AdoptionCommand
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.springframework.validation.Errors
import java.util.UUID

internal class AdoptionValidatorTest {

    companion object {
        private val VALID_UUID: String = UUID.randomUUID().toString()
    }

    private val adoptionValidator = AdoptionValidator()

    @Test
    @DisplayName("rejecting adoption command with invalid uuid")
    fun shouldRejectDueToInvalidUuid(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val adoptionCommand = AdoptionCommand().apply { uuid = "uuid" }
        val errors: Errors = mock()

        adoptionValidator.validate(adoptionCommand, errors)

        verify(errors).rejectValue("uuid", "adoption.error.uuid.invalid")
    }

    @Test
    @DisplayName("validating adoption command with valid uuid")
    fun shouldValidateWithValidUuid(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val adoptionCommand = AdoptionCommand().apply { uuid = VALID_UUID }
        val errors: Errors = mock()

        adoptionValidator.validate(adoptionCommand, errors)

        verify(errors, never()).rejectValue("uuid", "adoption.error.uuid.invalid")
    }
}
