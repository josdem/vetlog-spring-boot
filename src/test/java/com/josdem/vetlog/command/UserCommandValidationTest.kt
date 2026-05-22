/*
  Copyright 2026 Jose Morales contact@josdem.io

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

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
    fun `should accept username between 5 and 50 characters`() {
        val command =
            UserCommand().apply {
                username = "josdem"
            }

        val violations = validator.validate(command)
        assertTrue(violations.isEmpty(), "Username within range should pass validation")
    }

    @Test
    fun `should reject username shorter than 5 characters`() {
        val command =
            UserCommand().apply {
                username = "test"
            }

        val violation = validator.validate(command)

        assertTrue(
            violation.any { it.propertyPath.toString() == "username" },
            "Expected username size validation error",
        )
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
