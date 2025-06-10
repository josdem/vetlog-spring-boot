package com.josdem.vetlog.helper

import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.slf4j.LoggerFactory
import java.util.Locale

class LocaleResolverTest {
    private val logger = LoggerFactory.getLogger(LocaleResolverTest::class.java)
    private val localeResolver = LocaleResolver()
    private val request: HttpServletRequest = mock(HttpServletRequest::class.java)

    @Test
    fun `should get default locale`(testInfo: TestInfo) {
        logger.info("Running: {}", testInfo.displayName)
        val result = localeResolver.resolveLocale(request)
        assertEquals(Locale.of("en"), result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["en-US,en;q=0.8", "zh-cn,zh-tw"])
    fun `should get locale from headers`(headers: String) {
        logger.info("Running: getting english from headers")
        `when`(request.getHeader("Accept-Language")).thenReturn(headers)
        val result = localeResolver.resolveLocale(request)
        assertEquals(Locale.of("en"), result)
    }

    @Test
    fun `should get spanish from headers`(testInfo: TestInfo) {
        logger.info("Running: {}", testInfo.displayName)
        `when`(request.getHeader("Accept-Language")).thenReturn("es-MX,en-US;q=0.7,en;q=0.3")
        val result = localeResolver.resolveLocale(request)
        assertEquals(Locale.of("es"), result)
    }
}
