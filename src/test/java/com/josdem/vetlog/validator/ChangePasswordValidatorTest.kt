package com.josdem.vetlog.validator

import com.josdem.vetlog.command.ChangePasswordCommand
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.slf4j.LoggerFactory
import org.springframework.validation.Errors

internal class ChangePasswordValidatorTest {
    companion object {
        private val log = LoggerFactory.getLogger(ChangePasswordValidatorTest::class.java)
    }

    private val validator = ChangePasswordValidator()

    @Test
    fun `should reject since passwords are not equals`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val command =
            ChangePasswordCommand().apply {
                token = "token"
                password = "password"
                passwordConfirmation = "passwords"
            }
        val errors: Errors = mock()

        validator.validate(command, errors)

        verify(errors).rejectValue("password", "user.error.password.equals")
    }
}
