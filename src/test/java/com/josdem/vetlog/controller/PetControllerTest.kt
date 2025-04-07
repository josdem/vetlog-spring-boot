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

import com.josdem.vetlog.enums.PetStatus
import com.josdem.vetlog.enums.PetType
import com.josdem.vetlog.repository.PetRepository
import com.josdem.vetlog.repository.UserRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {
    companion object {
        val PET_UUID = UUID.randomUUID().toString()
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var petRepository: PetRepository

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
    fun `should register new pet`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        registerPet(PetStatus.IN_ADOPTION)
    }

    @Test
    @Transactional
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should show edit pet form`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        // Set up data before the test
        registerPet(PetStatus.IN_ADOPTION)
        val request =
            get("/pet/edit")
                .param("uuid", PET_UUID)
        // Edit test
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("petCommand"))
            .andExpect(model().attributeExists("breeds"))
            .andExpect(model().attributeExists("breedsByTypeUrl"))
            .andExpect(view().name("pet/edit"))
    }

    @Test
    @Transactional
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should update pet status`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        // Set up data before the test
        registerPet(PetStatus.IN_ADOPTION)
        val user =
            userRepository
                .findByUsername("josdem")
                .orElseThrow { RuntimeException("User not found") }
        val cremita =
            petRepository
                .findByUuid(PET_UUID)
                .orElseThrow { RuntimeException("Pet not found") }
        val request =
            multipart("/pet/update")
                .file(image)
                .with(csrf())
                .param("id", cremita.id.toString())
                .param("name", "Cremita")
                .param("uuid", PET_UUID)
                .param("birthDate", "2024-08-22")
                .param("dewormed", "true")
                .param("vaccinated", "true")
                .param("sterilized", "true")
                .param("breed", "11")
                .param("user", user.id.toString())
                .param("status", PetStatus.OWNED.toString())
                .param("type", PetType.DOG.toString())
        // Update test
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("message"))
            .andExpect(view().name("pet/edit"))
    }

    @Test
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should list pets`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        mockMvc
            .perform(get("/pet/list"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("pets"))
            .andExpect(model().attributeExists("defaultImage"))
            .andExpect(view().name("pet/list"))
    }

    @Test
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should list pets for adoption`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        mockMvc
            .perform(get("/pet/listForAdoption"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("pets"))
            .andExpect(view().name("pet/listForAdoption"))
    }

    @Test
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should give pets for adoption`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        mockMvc
            .perform(get("/pet/giveForAdoption"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("pets"))
            .andExpect(model().attributeExists("defaultImage"))
            .andExpect(view().name("pet/giveForAdoption"))
    }

    @Test
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should show create pet form`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        mockMvc
            .perform(get("/pet/create"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("petCommand"))
            .andExpect(model().attributeExists("breeds"))
            .andExpect(model().attributeExists("breedsByTypeUrl"))
            .andExpect(view().name("pet/create"))
    }

    @Test
    @Transactional
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should show error when deleting a pet due to is in adoption`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        registerPet(PetStatus.IN_ADOPTION)
        val request =
            get("/pet/delete")
                .param("uuid", PET_UUID)
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
    }

    @Test
    @Transactional
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun `should delete pet`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        // Register a pet with OWNED status
        registerPet(PetStatus.OWNED)
        // Perform the delete request
        val request =
            get("/pet/delete")
                .param("uuid", PET_UUID)
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("message"))
            .andExpect(view().name("pet/list"))
    }

    private fun registerPet(status: PetStatus) {
        val request =
            multipart("/pet/save")
                .file(image)
                .with(csrf())
                .param("name", "Cremita")
                .param("uuid", PET_UUID)
                .param("birthDate", "2024-08-22")
                .param("dewormed", "true")
                .param("vaccinated", "true")
                .param("sterilized", "true")
                .param("breed", "11")
                .param("user", "1")
                .param("status", status.toString())
                .param("type", PetType.DOG.toString())
        mockMvc
            .perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("pet/create"))
    }
}
