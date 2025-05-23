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

import com.josdem.vetlog.model.RegistrationCode
import com.josdem.vetlog.repository.RegistrationCodeRepository
import com.josdem.vetlog.service.impl.RegistrationServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import java.util.Optional
import kotlin.test.Test

internal class RegistrationServiceTest {
    private lateinit var service: RegistrationService

    @Mock
    private lateinit var localeService: LocaleService

    @Mock
    private lateinit var repository: RegistrationCodeRepository

    companion object {
        private val log = LoggerFactory.getLogger(RegistrationServiceTest::class.java)
        private const val TOKEN = "token"
        private const val EMAIL = "contact@josdem.io"
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        service = RegistrationServiceImpl(localeService, repository)
    }

    @Test
    fun `Getting user by token`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val registrationCode = RegistrationCode().apply { email = EMAIL }
        whenever(repository.findByToken(TOKEN)).thenReturn(Optional.of(registrationCode))
        assertEquals(EMAIL, service.findEmailByToken(TOKEN).get())
    }

    @Test
    fun `Generating token`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val token = service.generateToken(EMAIL)
        assertEquals(36, token.length)
        verify(repository).save(any<RegistrationCode>())
    }
}
