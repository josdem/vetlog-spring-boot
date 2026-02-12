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

package com.josdem.vetlog.controller

import com.josdem.vetlog.controller.PetControllerTest.Companion.PET_UUID
import com.josdem.vetlog.enums.PetStatus
import com.josdem.vetlog.enums.PetType
import com.josdem.vetlog.enums.WeightUnits
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class TelephoneControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val image =
        MockMultipartFile(
            "mockImage",
            "image.jpg",
            "image/jpeg",
            "image".toByteArray(),
        )

    @BeforeEach
    fun setUp() {
        mockMvc =
            MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply { springSecurity() }
                .build()
    }

    @Test
    @Transactional
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should show adopting form`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        registerPet()
        val request =
            get("/telephone/adopt")
                .param("uuid", PET_UUID)
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("telephone/adopt"))
            .andExpect(model().attributeExists("pet"))
            .andExpect(model().attributeExists("telephoneCommand"))
            .andExpect(status().isOk())
    }

    @Test
    @Transactional
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should not save adoption due to invalid phone number`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        registerPet()
        val request =
            post("/telephone/save")
                .with(csrf())
                .param("uuid", PET_UUID)
                .param("mobile", "123")
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("telephone/adopt"))
            .andExpect(model().attributeExists("pet"))
            .andExpect(model().attributeExists("telephoneCommand"))
            .andExpect(status().isOk())
    }

    private fun registerPet() {
        val request =
            multipart("/pet/save")
                .file(image)
                .with(csrf())
                .param("name", "Cremita")
                .param("uuid", PET_UUID)
                .param("birthDate", "2024-08-22")
                .param("sterilized", "true")
                .param("breed", "11")
                .param("user", "1")
                .param("weight", "6.50")
                .param("unit", WeightUnits.KG.name)
                .param("status", PetStatus.OWNED.toString())
                .param("type", PetType.DOG.toString())
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("pet/create"))
    }
}
