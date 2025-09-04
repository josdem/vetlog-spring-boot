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

class VaccineFormatterTest {
    private val log = LoggerFactory.getLogger(this::class.java)

    @InjectMocks
    private val vaccineFormatter = VaccineFormatter()

    @Mock
    private lateinit var locale: Locale

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `should not format Puppy irrespective of language`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")
        assertEquals("Puppy", vaccineFormatter.format("Puppy", locale))
        whenever(locale.language).thenReturn("en")
        assertEquals("Puppy", vaccineFormatter.format("Puppy", locale))
    }

    @Test
    fun `should format C4CV if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Quintuple Canina", vaccineFormatter.format("C4CV", locale))
    }

    @Test
    fun `should not format C4CV if locale is English`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("en")

        assertEquals("C4CV", vaccineFormatter.format("C4CV", locale))
    }

    @Test
    fun `should format C6CV if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Sextuple Canina", vaccineFormatter.format("C6CV", locale))
    }

    @Test
    fun `should format deworming if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Desparasitación", vaccineFormatter.format("Deworming", locale))
    }

    @Test
    fun `should format rabies if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Rabia", vaccineFormatter.format("Rabies", locale))
    }

    @Test
    fun `should not format Bordetella`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("en")

        assertEquals("Bordetella", vaccineFormatter.format("Bordetella", locale))
    }

    @Test
    fun `should format FVRCP if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Trivalente Felina", vaccineFormatter.format("FVRCP", locale))
    }

    @Test
    fun `should not format C6CV if locale is English`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("en")

        assertEquals("C6CV", vaccineFormatter.format("C6CV", locale))
    }

    @Test
    fun `should format FeLV if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Leucemia Felina", vaccineFormatter.format("FeLV", locale))
    }

    @Test
    fun `should format pending status if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Pendiente", vaccineFormatter.formatStatus("PENDING", locale))
    }

    @Test
    fun `should format applied status if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Aplicada", vaccineFormatter.formatStatus("APPLIED", locale))
    }

    @Test
    fun `should format applied status if locale is English`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("en")

        assertEquals("APPLIED", vaccineFormatter.formatStatus("APPLIED", locale))
    }
}
