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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val log = LoggerFactory.getLogger(this::class.java)

    @Test
    fun `should show my pet location`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val request =
            get("/geolocation/location/338/37.7749/-122.4194")
        mockMvc
            .perform(
                request,
            ).andExpect(status().isOk)
    }

    @Test
    fun `should store my pet list`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val request =
            get("/geolocation/location/338,339")
        mockMvc
            .perform(
                request,
            ).andExpect(status().isOk)

        assertTrue {
            ApplicationCache.locations.size == 2 &&
                ApplicationCache.locations.keys
                    .toList()
                    .containsAll(listOf(338, 339))
        }
    }
}
