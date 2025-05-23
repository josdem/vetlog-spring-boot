package com.josdem.vetlog.validator

import com.josdem.vetlog.command.UserCommand
import com.josdem.vetlog.model.User
import com.josdem.vetlog.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
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
import org.slf4j.LoggerFactory
import org.springframework.validation.Errors
import java.util.Optional

internal class UserValidatorTest {
    companion object {
        private const val COUNTRY_CODE = "+52"
        private const val MOBILE = "1234567890"
        private val log = LoggerFactory.getLogger(UserValidatorTest::class.java)
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
    fun `should validate an user`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val userCommand = getUserCommand()
        validator.validate(userCommand, errors)
        verify(errors, never()).rejectValue(any(), any())
    }

    @Test
    fun `should reject user since password does not match`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val userCommand =
            getUserCommand().apply {
                passwordConfirmation = "passwords"
            }
        validator.validate(userCommand, errors)
        verify(errors).rejectValue("password", "user.error.password.equals")
    }

    @ParameterizedTest
    @ValueSource(strings = ["pass-word", "pass_word", "password."])
    fun `should accept dash character in password`(
        password: String,
        testInfo: TestInfo,
    ) {
        log.info(testInfo.displayName)
        val userCommand =
            getUserCommand().apply {
                this.password = password
                this.passwordConfirmation = password
            }
        validator.validate(userCommand, errors)
        verify(errors, never()).rejectValue(any(), any())
    }

    @Test
    fun `should not duplicate users`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val userCommand = getUserCommand()
        whenever(userRepository.findByUsername("josdem")).thenReturn(Optional.of(User()))
        validator.validate(userCommand, errors)
        verify(errors).rejectValue("username", "user.error.duplicated.username")
    }

    @Test
    fun `should not duplicate users by email`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val userCommand = getUserCommand()
        whenever(userRepository.findByEmail("contact@josdem.io")).thenReturn(Optional.of(User()))
        validator.validate(userCommand, errors)
        verify(errors).rejectValue("email", "user.error.duplicated.email")
    }

    @Test
    fun `should reject user since invalid mobile`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val userCommand = getUserCommand().apply { mobile = "notValidPhoneNumber" }
        validator.validate(userCommand, errors)
        verify(errors).rejectValue("mobile", "user.error.mobile")
    }

    @Test
    fun `should not duplicate users by country code and mobile`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
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
