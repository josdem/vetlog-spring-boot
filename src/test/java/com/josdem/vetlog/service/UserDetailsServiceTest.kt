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

import com.josdem.vetlog.enums.Role
import com.josdem.vetlog.exception.BusinessException
import com.josdem.vetlog.model.User
import com.josdem.vetlog.repository.UserRepository
import com.josdem.vetlog.service.impl.UserDetailsServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import kotlin.test.Test
import java.util.Optional

internal class UserDetailsServiceTest {
    private lateinit var service: UserDetailsServiceImpl

    @Mock
    private lateinit var userRepository: UserRepository

    private val user = User()

    companion object {
        private val log = LoggerFactory.getLogger(UserDetailsServiceTest::class.java)
        private const val USERNAME = "josdem"
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        service = UserDetailsServiceImpl(userRepository)
    }

    @Test
    @DisplayName("Loading user by username")
    fun shouldLoadUserByUsername(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        setBasicUserData()
        user.isEnabled = true
        user.isAccountNonExpired = true
        user.isAccountNonLocked = true
        user.isCredentialsNonExpired = true
        whenever(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user))

        val result = service.loadUserByUsername(USERNAME)

        Assertions.assertEquals(user.username, result.username)
        Assertions.assertEquals(user.password, result.password)
        Assertions.assertEquals(user.isEnabled, result.isEnabled)
    }

    @Test
    @DisplayName("Not searching for authorities since user does not exist")
    fun shouldNotSearchForAuthoritiesDueToUserNotFound(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        assertThrows<BusinessException> { service.loadUserByUsername("thisUserDoesNotExist") }
    }

    @Test
    @DisplayName("Not returning user since it is not enabled")
    fun shouldNotReturnUserSinceIsNotEnabled(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        setBasicUserData()
        user.isEnabled = false
        whenever(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user))
        assertThrows<BusinessException> { service.loadUserByUsername(USERNAME) }
    }

    private fun setBasicUserData() {
        user.username = USERNAME
        user.password = "password"
        user.role = Role.USER
    }
}
