package com.josdem.vetlog.controller

import com.josdem.vetlog.controller.PetControllerTest.Companion.PET_UUID
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

import com.josdem.vetlog.enums.PetStatus
import com.josdem.vetlog.enums.PetType
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
internal class AdoptionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val image = MockMultipartFile(
        "mockImage",
        "image.jpg",
        "image/jpeg",
        "image".toByteArray()
    )

    private val log = LoggerFactory.getLogger(this::class.java)

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply { springSecurity() }
            .build()
    }

    @Test
    @Transactional
    @DisplayName("showing description for adoption form")
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun shouldShowDescriptionForAdoptionForm(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        registerPet()
        val request = get("/adoption/descriptionForAdoption")
            .param("uuid", PET_UUID)
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("adoption/descriptionForAdoption"))
            .andExpect(model().attributeExists("pet"))
            .andExpect(model().attributeExists("adoptionCommand"))
            .andExpect(status().isOk())
    }

    @Test
    @Transactional
    @DisplayName("saving adoption description")
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun shouldSaveAdoptionDescription(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        registerPet()
        val request = post("/adoption/save")
            .with(csrf())
            .param("uuid", PET_UUID)
            .param("description", "Cremita is a lovely dog")
            .param("status", PetStatus.IN_ADOPTION.toString())
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("pet/listForAdoption"))
    }

    private fun registerPet()  {
        val request = multipart("/pet/save")
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
            .param("status", PetStatus.OWNED.toString())
            .param("type", PetType.DOG.toString())
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("pet/create"))
    }
}
