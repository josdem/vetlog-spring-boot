package com.josdem.vetlog.command

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UsernameCommandValidationTest {
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun `should accept username with 5 characters`() {
        val command = UsernameCommand().setUsername("Orion")

        val violations = validator.validate(command)

        assertTrue(
            violations.isEmpty(),
            "Username with 5 characters should pass validation",
        )
    }

    @Test
    fun `should reject username shorter than 5 characters`() {
        val command = UsernameCommand().setUsername("test")

        val violations = validator.validate(command)

        assertTrue(
            violations.any { it.propertyPath.toString() == "username" },
            "Expected username size validation error",
        )
    }
}
