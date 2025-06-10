package com.josdem.vetlog.util

import com.josdem.vetlog.service.LocaleService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.Locale

@ExtendWith(MockitoExtension::class)
class DateFormatterTest {
    @Mock
    private lateinit var localeService: LocaleService

    private lateinit var dateFormatter: DateFormatter

    @BeforeEach
    fun setup() {
        dateFormatter = DateFormatter(localeService)
    }

    @Test
    fun `should format date for US locale`() {
        val date = LocalDateTime.parse("2021-11-17T10:15:00")
        val usLocale = Locale.forLanguageTag("en-US")
        val expectedFormat = "MM/dd/yyyy"
        val expectedDateString = "11/17/2021"

        whenever(localeService.getMessage("format.date", usLocale))
            .thenReturn(expectedFormat)

        val formattedDate = dateFormatter.formatToDate(date, usLocale)

        assertEquals(expectedDateString, formattedDate)
    }

    @Test
    fun `should format date for ES locale`() {
        val date = LocalDateTime.parse("1999-08-18T10:14:00")
        val esLocale = Locale.forLanguageTag("es-ES")
        val expectedFormat = "dd/MM/yyyy"
        val expectedDateString = "18/08/1999"

        whenever(localeService.getMessage("format.date", esLocale))
            .thenReturn(expectedFormat)

        val formattedDate = dateFormatter.formatToDate(date, esLocale)

        assertEquals(expectedDateString, formattedDate)
    }
}
