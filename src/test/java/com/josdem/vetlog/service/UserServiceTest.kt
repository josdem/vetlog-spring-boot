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

import com.josdem.vetlog.binder.UserBinder
import com.josdem.vetlog.command.Command
import com.josdem.vetlog.config.ApplicationProperties
import com.josdem.vetlog.exception.UserNotFoundException
import com.josdem.vetlog.model.User
import com.josdem.vetlog.repository.UserRepository
import com.josdem.vetlog.service.impl.UserServiceImpl
import com.josdem.vetlog.util.UserContextHolderProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import kotlin.test.Test
import java.util.Optional

internal class UserServiceTest {
    private lateinit var service: UserService
    private val user = User()

    @Mock
    private lateinit var userBinder: UserBinder

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var provider: UserContextHolderProvider

    @Mock
    private lateinit var applicationProperties: ApplicationProperties

    @Mock
    private lateinit var emailService: EmailService

    companion object {
        private val log = LoggerFactory.getLogger(UserServiceTest::class.java)
        private const val USERNAME = "josdem"
        private const val EMAIL = "contact@josdem.io"
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        service = UserServiceImpl(userBinder, userRepository, provider, applicationProperties, emailService)
    }

    @Test
    @DisplayName("Getting user by username")
    fun shouldGetUserByUsername(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user))
        val result = service.getByUsername(USERNAME)
        Assertions.assertEquals(user, result)
    }

    @Test
    @DisplayName("Not finding user by username")
    fun shouldNotGetUserByUsername(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty())
        assertThrows<UserNotFoundException> { service.getByUsername(USERNAME) }
    }

    @Test
    @DisplayName("Getting user by email")
    fun shouldGetUserByEmail(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user))
        val result = service.getByEmail(EMAIL)
        Assertions.assertEquals(user, result)
    }

    @Test
    @DisplayName("Not finding user by email")
    fun shouldNotGetUserByEmail(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty())
        assertThrows<UserNotFoundException> { service.getByEmail(EMAIL) }
    }

    @Test
    @DisplayName("Saving a user")
    fun shouldSaveAnUser(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val command: Command = mock()
        user.email = EMAIL
        whenever(userBinder.bindUser(command)).thenReturn(user)

        val result = service.save(command)

        verify(emailService).sendWelcomeEmail(user)
        verify(userRepository).save(user)
        Assertions.assertEquals(user, result)
    }

    @Test
    @DisplayName("Disabling a user due to country code")
    fun shouldDisableAnUser(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val command: Command = mock()
        user.email = EMAIL
        user.countryCode = "+countryCodeOne"
        whenever(applicationProperties.countryCodes).thenReturn(mutableListOf("+countryCodeOne"))
        whenever(userBinder.bindUser(command)).thenReturn(user)

        val result = service.save(command)

        verify(userRepository).save(user)
        Assertions.assertEquals(user, result)
        Assertions.assertFalse(user.isEnabled, "User should be disabled")
    }

    @Test
    @DisplayName("Getting current user")
    fun shouldGetCurrentUser(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val authentication: Authentication = mock()
        whenever(authentication.name).thenReturn(USERNAME)
        whenever(provider.authentication).thenReturn(authentication)
        whenever(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user))
        Assertions.assertEquals(user, service.getCurrentUser())
    }

    @Test
    @DisplayName("Not finding current user")
    fun shouldNotGetCurrentUser(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val authentication: Authentication = mock()
        whenever(authentication.name).thenReturn(USERNAME)
        whenever(provider.authentication).thenReturn(authentication)
        whenever(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty())
        assertThrows<UserNotFoundException> { service.getCurrentUser() }
    }
}
