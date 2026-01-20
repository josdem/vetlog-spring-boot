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

import com.josdem.vetlog.config.TemplateProperties
import com.josdem.vetlog.exception.BusinessException
import com.josdem.vetlog.exception.UserNotFoundException
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.User
import com.josdem.vetlog.service.impl.EmailServiceImpl
import com.josdem.vetlog.util.UserUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.Locale
import kotlin.test.Test

internal class EmailServiceTest {
    private lateinit var emailService: EmailService

    @Mock
    private lateinit var restService: RestService

    @Mock
    private lateinit var petService: PetService

    @Mock
    private lateinit var localeService: LocaleService

    @Mock
    private lateinit var templateProperties: TemplateProperties

    @Mock
    private lateinit var userUtil: UserUtil

    private val user = User()
    private val pet = Pet()

    companion object {
        private val log = LoggerFactory.getLogger(EmailServiceTest::class.java)
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        emailService = EmailServiceImpl(restService, localeService, templateProperties, petService, userUtil)
    }

    @Test
    fun `Sending a welcome email`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(templateProperties.welcome).thenReturn("welcome.ftl")
        whenever(userUtil.isValid(user)).thenReturn(true)
        user.firstName = "Jose"
        user.email = "contact@josdem.io"

        emailService.sendWelcomeEmail(user, Locale.ENGLISH)

        verify(localeService).getMessage("user.welcome.message", Locale.ENGLISH)
        verify(restService).sendMessage(any())
    }

    @Test
    fun `Not sending a welcome email due to an exception`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(userUtil.isValid(user)).thenReturn(true)
        whenever(restService.sendMessage(any())).thenThrow(IOException("Error"))

        assertThrows<BusinessException> {
            emailService.sendWelcomeEmail(user, Locale.ENGLISH)
        }
    }

    @Test
    fun `Not sending email if user is not enabled`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        user.isEnabled = false

        emailService.sendWelcomeEmail(user, Locale.ENGLISH)

        verify(restService, never()).sendMessage(any())
    }

    @Test
    fun `Sending a pulling up email`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(templateProperties.pullingUp).thenReturn("pulling-up.ftl")
        user.firstName = "abc"
        user.email = "abc@xyz.io"
        pet.id = 338L
        pet.user = user
        whenever(petService.getPetById(any())).thenReturn(pet)
        emailService.sendPullingUpEmail(1L, Locale.ENGLISH)

        verify(localeService).getMessage("email.subject", Locale.forLanguageTag("es"))
        verify(restService).sendMessage(any())
    }

    @Test
    fun `Not sending a pulling up email due to an exception`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(templateProperties.pullingUp).thenReturn("pulling-up.ftl")
        user.firstName = "abc"
        user.email = "abc@xyz.io"
        pet.id = 338L
        pet.user = user
        whenever(petService.getPetById(any())).thenReturn(pet)
        whenever(restService.sendMessage(any())).thenThrow(IOException("Error"))

        assertThrows<BusinessException> {
            emailService.sendPullingUpEmail(1L, Locale.ENGLISH)
        }
    }

    @Test
    fun `Not sending a pulling up email if user is not found`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        pet.id = 338L
        whenever(petService.getPetById(1L)).thenReturn(pet)
        assertThrows<UserNotFoundException> {
            emailService.sendPullingUpEmail(1L, Locale.ENGLISH)
        }
        verify(restService, never()).sendMessage(any())
    }
}
