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
import com.josdem.vetlog.service.PetService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class GoogleMapControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var petService: PetService

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val log = LoggerFactory.getLogger(this::class.java)

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
    fun `showMap should return Map view with API key`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        registerPet()

        val pet = petService.getPetByUuid(PET_UUID)

        mockMvc
            .perform(
                get("/map/view")
                    .param("id", pet.id.toString()),
            ).andExpect(status().isOk)
            .andExpect(view().name("map/map"))
            .andExpect(model().attributeExists("apiKey"))
            .andExpect(model().attributeExists("latitude"))
            .andExpect(model().attributeExists("longitude"))
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

    @Test
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should list pets`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        mockMvc
            .perform(get("/map/list"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("pets"))
            .andExpect(model().attributeExists("defaultImage"))
            .andExpect(view().name("map/list"))
    }
}
