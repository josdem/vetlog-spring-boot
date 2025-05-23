/*
  Copyright 2025 Jose Morales contact@josdem.io

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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.Locale
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
    fun `Sending activation account token`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL))
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user))

        service.confirmAccountForToken(TOKEN)

        assertTrue(user.isEnabled)
        verify(userRepository).save(user)
    }

    @Test
    fun `Not sending activation account token due to token not existing`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        assertThrows(VetlogException::class.java) { service.confirmAccountForToken(TOKEN) }
    }

    @Test
    fun `Not sending activation account token due to user not found`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL))
        assertThrows(UserNotFoundException::class.java) { service.confirmAccountForToken(TOKEN) }
    }

    @Test
    @Throws(IOException::class)
    fun `Generating token to change password`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        user.isEnabled = true
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user))
        whenever(registrationService.generateToken(EMAIL)).thenReturn(TOKEN)

        service.generateRegistrationCodeForEmail(EMAIL, Locale.ENGLISH)
        verify(restService).sendMessage(any<MessageCommand>())
    }

    @Test
    fun `Not sending change password token due to user not found`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        assertThrows(UserNotFoundException::class.java) { service.generateRegistrationCodeForEmail(EMAIL, Locale.ENGLISH) }
    }

    @Test
    fun `Not sending change password token due to user not enabled`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        user.isEnabled = false
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user))
        assertThrows(VetlogException::class.java) { service.generateRegistrationCodeForEmail(EMAIL, Locale.ENGLISH) }
    }

    @Test
    fun `Validating a token`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(repository.findByToken(TOKEN)).thenReturn(Optional.of(RegistrationCode()))
        assertTrue(service.validateToken(TOKEN))
    }

    @Test
    fun `Finding an invalid token`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        assertFalse(service.validateToken(TOKEN))
    }

    @Test
    fun `Changing password`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val changePasswordCommand =
            ChangePasswordCommand().apply {
                token = TOKEN
                password = "password"
            }
        whenever(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL))
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user))

        service.changePassword(changePasswordCommand)

        assertEquals(60, user.password.length)
        verify(userRepository).save(user)
    }
}
