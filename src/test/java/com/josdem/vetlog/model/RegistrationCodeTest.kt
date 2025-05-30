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

package com.josdem.vetlog.model

import com.josdem.vetlog.enums.RegistrationCodeStatus
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class RegistrationCodeTest {
    private val registrationCode: RegistrationCode = RegistrationCode()

    private val log = LoggerFactory.getLogger(this::class.java)

    @Test
    fun `should generate registration code`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        assertEquals(7, ChronoUnit.DAYS.between(registrationCode.dateCreated, LocalDateTime.now().plusDays(7)))
        assertEquals(36, registrationCode.token.length)
        assertTrue { registrationCode.isValid }
    }

    @Test
    fun `should invalidate token`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        registrationCode.status = RegistrationCodeStatus.INVALID
        assertFalse { registrationCode.isValid }
    }
}
