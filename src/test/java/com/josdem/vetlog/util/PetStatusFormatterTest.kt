/*
  Copyright 2026 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import java.util.Locale

class PetStatusFormatterTest {
    private val log = LoggerFactory.getLogger(this::class.java)

    @InjectMocks
    private val petStatusFormatter = PetStatusFormatter()

    @Mock
    private lateinit var locale: Locale

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `should format Owned based on user's language`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")
        assertEquals("Con Familia", petStatusFormatter.format("Owned", locale))
        whenever(locale.language).thenReturn("en")
        assertEquals("Owned", petStatusFormatter.format("Owned", locale))
    }

    @Test
    fun `should format In Adoption based on user's language`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")
        assertEquals("En Adopción", petStatusFormatter.format("In Adoption", locale))
        whenever(locale.language).thenReturn("en")
        assertEquals("In Adoption", petStatusFormatter.format("In Adoption", locale))
    }

    @Test
    fun `should format Adopted based on user's language`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")
        assertEquals("Adoptado", petStatusFormatter.format("Adopted", locale))
        whenever(locale.language).thenReturn("en")
        assertEquals("Adopted", petStatusFormatter.format("Adopted", locale))
    }

    @Test
    fun `should format Deceased based on user's language`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")
        assertEquals("En el Cielo", petStatusFormatter.format("Deceased", locale))
        whenever(locale.language).thenReturn("en")
        assertEquals("Deceased", petStatusFormatter.format("Deceased", locale))
    }
}
