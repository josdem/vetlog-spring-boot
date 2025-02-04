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

import com.josdem.vetlog.helper.LocaleResolver
import jakarta.servlet.http.HttpServletRequest
import com.josdem.vetlog.service.impl.LocaleServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInfo
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import java.util.Locale
import kotlin.test.Test

internal class LocaleServiceTest {

    private lateinit var service: LocaleService

    @Mock
    private lateinit var messageSource: MessageSource

    @Mock
    private lateinit var localeResolver: LocaleResolver

    private val code = "code"

    companion object {
        private val log = LoggerFactory.getLogger(LocaleServiceTest::class.java)
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        service = LocaleServiceImpl(messageSource, localeResolver)
    }

    @Test
    @DisplayName("Getting message by request")
    fun shouldGetMessageByRequest(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val request: HttpServletRequest = mock()

        whenever(messageSource.getMessage(code, null, localeResolver.resolveLocale(request)))
            .thenReturn("message")

        Assertions.assertEquals("message", service.getMessage(code, request))
    }

    @Test
    @DisplayName("Getting message by code")
    fun shouldGetMessageByCode(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)

        whenever(messageSource.getMessage(code, null, Locale.of("en"))).thenReturn("message")

        Assertions.assertEquals("message", service.getMessage(code))
    }
}

