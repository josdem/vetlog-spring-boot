package com.josdem.vetlog.command

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserCommandValidationTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun `should reject password shorter than 7 characters`() {
        val command =
            UserCommand().apply {
                password = "test"
                passwordConfirmation = "test"
            }

        val violation = validator.validate(command)

        assertTrue(
            violation.any { it.propertyPath.toString() == "password" },
            "Expected password size validation error",
        )
    }

    @Test
    fun `should accept password between 7 and 50 characters`() {
        val command =
            UserCommand().apply {
                password = "password"
                passwordConfirmation = "password"
            }

        val violations = validator.validate(command)
        assertTrue(violations.isEmpty(), "Password within range should pass validation")
    }

    @Test
    fun `should reject password longer than 50 characters`() {
        val longPassword = "a".repeat(51)
        val command =
            UserCommand().apply {
                password = longPassword
                passwordConfirmation = longPassword
            }

        val violations = validator.validate(command)

        assertTrue(
            violations.any { it.propertyPath.toString() == "password" },
            "Expected password too long validation error",
        )
    }
}
