/*
Copyright 2024 Jose Morales contact@josdem.io

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
package com.josdem.vetlog.service

import com.josdem.vetlog.command.ChangePasswordCommand
import com.josdem.vetlog.command.MessageCommand
import com.josdem.vetlog.exception.UserNotFoundException
import com.josdem.vetlog.exception.VetlogException
import com.josdem.vetlog.model.RegistrationCode
import com.josdem.vetlog.model.User
import com.josdem.vetlog.repository.RegistrationCodeRepository
import com.josdem.vetlog.repository.UserRepository
import com.josdem.vetlog.service.impl.RecoveryServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.Optional
import kotlin.test.Test

internal class RecoveryServiceTest {
    private lateinit var service: RecoveryService
    private val user = User()

    @Mock
    private lateinit var restService: RestService

    @Mock
    private lateinit var registrationService: RegistrationService

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var repository: RegistrationCodeRepository

    @Mock
    private lateinit var localeService: LocaleService

    companion object {
        private val log = LoggerFactory.getLogger(RecoveryServiceTest::class.java)
        private const val TOKEN = "token"
        private const val EMAIL = "contact@josdem.io"
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        service = RecoveryServiceImpl(restService, registrationService, userRepository, repository, localeService)
    }

    @Test
    @DisplayName("Sending activation account token")
    fun shouldSendActivationAccountToken(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL))
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user))

        service.confirmAccountForToken(TOKEN)

        Assertions.assertTrue(user.isEnabled)
        verify(userRepository).save(user)
    }

    @Test
    @DisplayName("Not sending activation account token due to token not existing")
    fun shouldNotSendActivationAccountTokenDueToInvalidToken(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        assertThrows<VetlogException> { service.confirmAccountForToken(TOKEN) }
    }

    @Test
    @DisplayName("Not sending activation account token due to user not found")
    fun shouldNotSendActivationAccountTokenDueToUserNotFound(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL))
        assertThrows<UserNotFoundException> { service.confirmAccountForToken(TOKEN) }
    }

    @Test
    @DisplayName("Generating token to change password")
    @Throws(IOException::class)
    fun shouldSendChangePasswordToken(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        user.isEnabled = true
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user))
        whenever(registrationService.generateToken(EMAIL)).thenReturn(TOKEN)

        service.generateRegistrationCodeForEmail(EMAIL)
        verify(restService).sendMessage(any<MessageCommand>())
    }

    @Test
    @DisplayName("Not sending change password token due to user not found")
    fun shouldNotSendChangePasswordTokenDueToUserNotFound(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        assertThrows<UserNotFoundException> { service.generateRegistrationCodeForEmail(EMAIL) }
    }

    @Test
    @DisplayName("Not sending change password token due to user not enabled")
    fun shouldNotSendChangePasswordTokenDueToUserNotEnabled(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        user.isEnabled = false
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user))
        assertThrows<VetlogException> { service.generateRegistrationCodeForEmail(EMAIL) }
    }

    @Test
    @DisplayName("Validating a token")
    fun shouldValidateToken(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(repository.findByToken(TOKEN)).thenReturn(Optional.of(RegistrationCode()))
        Assertions.assertTrue(service.validateToken(TOKEN))
    }

    @Test
    @DisplayName("Finding an invalid token")
    fun shouldFindInvalidToken(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        Assertions.assertFalse(service.validateToken(TOKEN))
    }

    @Test
    @DisplayName("Changing password")
    fun shouldChangePassword(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val changePasswordCommand = ChangePasswordCommand().apply {
            token = TOKEN
            password = "password"
        }
        whenever(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL))
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user))

        service.changePassword(changePasswordCommand)

        Assertions.assertEquals(60, user.password.length)
        verify(userRepository).save(user)
    }
}
