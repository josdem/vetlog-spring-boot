package com.josdem.vetlog.validator

import com.josdem.vetlog.command.UserCommand
import com.josdem.vetlog.model.User
import com.josdem.vetlog.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.validation.Errors
import java.util.Optional

internal class UserValidatorTest {

    companion object {
        private const val COUNTRY_CODE = "+52"
        private const val MOBILE = "1234567890"
    }

    private lateinit var validator: UserValidator

    @Mock
    private lateinit var userRepository: UserRepository

    private val errors: Errors = mock()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        validator = UserValidator(userRepository)
    }

    @Test
    @DisplayName("validating an user")
    fun shouldValidateAnUser(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val userCommand = getUserCommand()
        validator.validate(userCommand, errors)
        verify(errors, never()).rejectValue(any(), any())
    }

    @Test
    @DisplayName("rejecting an user since passwords do not match")
    fun shouldRejectUserSincePasswordDoesNotMatch(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val userCommand = getUserCommand().apply {
            passwordConfirmation = "passwords"
        }
        validator.validate(userCommand, errors)
        verify(errors).rejectValue("password", "user.error.password.equals")
    }

    @DisplayName("accepting passwords")
    @ParameterizedTest
    @ValueSource(strings = ["pass-word", "pass_word", "password."])
    fun shouldAcceptDashCharacterInPassword(password: String, testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val userCommand = getUserCommand().apply {
            this.password = password
            this.passwordConfirmation = password
        }
        validator.validate(userCommand, errors)
        verify(errors, never()).rejectValue(any(), any())
    }

    @Test
    @DisplayName("not duplicating users")
    fun shouldNotDuplicateUsers(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val userCommand = getUserCommand()
        whenever(userRepository.findByUsername("josdem")).thenReturn(Optional.of(User()))
        validator.validate(userCommand, errors)
        verify(errors).rejectValue("username", "user.error.duplicated.username")
    }

    @Test
    @DisplayName("not duplicating users by email")
    fun shouldNotDuplicateUsersByEmail(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val userCommand = getUserCommand()
        whenever(userRepository.findByEmail("contact@josdem.io")).thenReturn(Optional.of(User()))
        validator.validate(userCommand, errors)
        verify(errors).rejectValue("email", "user.error.duplicated.email")
    }

    @Test
    @DisplayName("rejecting an user since mobile format is not valid")
    fun shouldRejectUserSinceInvalidMobile(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val userCommand = getUserCommand().apply { mobile = "notValidPhoneNumber" }
        validator.validate(userCommand, errors)
        verify(errors).rejectValue("mobile", "user.error.mobile")
    }

    @Test
    @DisplayName("not duplicating users by country code plus mobile")
    fun shouldNotDuplicateUsersByCountryCodeAndMobile(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        val userCommand = getUserCommand()
        whenever(userRepository.findByCountryCodeAndMobile(COUNTRY_CODE, MOBILE))
            .thenReturn(Optional.of(User()))
        validator.validate(userCommand, errors)
        verify(errors).rejectValue("mobile", "user.error.duplicated.mobile")
    }

    private fun getUserCommand(): UserCommand =
        UserCommand().apply {
            username = "josdem"
            password = "password"
            passwordConfirmation = "password"
            firstname = "Jose"
            lastname = "Morales"
            email = "contact@josdem.io"
            countryCode = COUNTRY_CODE
            mobile = MOBILE
        }
}
