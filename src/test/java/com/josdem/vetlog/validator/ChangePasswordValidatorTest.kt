package com.josdem.vetlog.validator

import com.josdem.vetlog.command.ChangePasswordCommand
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.validation.Errors

internal class ChangePasswordValidatorTest {

    private val validator = ChangePasswordValidator()

    @Test
    @DisplayName("not validating since password does not match")
    fun shouldRejectSincePasswordsAreNotEquals(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val command = ChangePasswordCommand().apply {
            token = "token"
            password = "password"
            passwordConfirmation = "passwords"
        }
        val errors: Errors = mock()

        validator.validate(command, errors)

        verify(errors).rejectValue("password", "user.error.password.equals")
    }
}
