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

package com.josdem.vetlog.controller

import com.josdem.vetlog.cache.ApplicationCache
import com.josdem.vetlog.model.Location
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.User
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.TestMethodOrder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class LocationControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var emailService: com.josdem.vetlog.service.EmailService

    @MockitoBean
    private lateinit var petservice: com.josdem.vetlog.service.PetService

    private val log = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun setupCache() {
        // Clear cache first
        ApplicationCache.locations.clear()

        // Populate cache like storePets would have done
        ApplicationCache.locations[338] = Location(0.0, 0.0)
        ApplicationCache.locations[339] = Location(0.0, 0.0)
    }

    @Test
    @Order(1)
    fun `should store my pet list`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val request =
            get("/geolocation/store/338,339")
        mockMvc
            .perform(
                request,
            ).andExpect(status().isOk)

        assertEquals(2, ApplicationCache.locations.size, "Expected 2 locations in the cache")
        assertTrue(
            ApplicationCache.locations.keys.containsAll(listOf(338, 339)),
            "Expected cache to contain keys 338 and 339",
        )
    }

    @Test
    fun `should send pulling up email notification`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val user = User()
        user.firstName = "abc"
        user.email = "abc@xyz.io"
        val pet = Pet()
        pet.id = 338L
        pet.user = user
        val request =
            get("/geolocation/pullup/${pet.id}")

        mockMvc
            .perform(request)
            .andExpect(status().isOk)
    }
}
