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

import com.josdem.vetlog.service.AdoptionServiceTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory

internal class TemplateLocaleResolverTest {
    companion object {
        private val log = LoggerFactory.getLogger(AdoptionServiceTest::class.java)
        private const val TEMPLATE = "template.ftl"
    }

    @Test
    fun `should get template with locale es`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val locale = "es"
        val expected = "template_es.ftl"

        val result = TemplateLocaleResolver.getTemplate(TEMPLATE, locale)
        assertEquals(expected, result, "should get template with locale es")
    }

    @Test
    fun `should get template with locale en`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val locale = "en"

        val result = TemplateLocaleResolver.getTemplate(TEMPLATE, locale)
        assertEquals(TEMPLATE, result, "should get template with locale en")
    }

    @Test
    fun `should get template even with locale as null`(testInfo: TestInfo) {
        log.info(testInfo.displayName)

        val result = TemplateLocaleResolver.getTemplate(TEMPLATE, null)
        assertEquals(TEMPLATE, result, "should get template with locale en")
    }
}
