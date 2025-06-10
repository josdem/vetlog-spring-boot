package com.josdem.vetlog.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import java.util.Locale

@ExtendWith(MockitoExtension::class)
class VaccineFormatterTest {
    private val log = LoggerFactory.getLogger(this::class.java)

    @InjectMocks
    private val vaccineFormatter = VaccineFormatter()

    @Mock
    private lateinit var locale: Locale

    @Test
    fun `should format DA2PP if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Quintuple Canina", vaccineFormatter.format("DA2PP", locale))
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
    fun `should format canine influenza if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Influenza Canina", vaccineFormatter.format("Canine Influenza", locale))
    }

    @Test
    fun `should format FVRCP if locale is Spanish`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("es")

        assertEquals("Tripe Felina", vaccineFormatter.format("FVRCP", locale))
    }

    @Test
    fun `should not format DA2PP if locale is English`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(locale.language).thenReturn("en")

        assertEquals("DA2PP", vaccineFormatter.format("DA2PP", locale))
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
