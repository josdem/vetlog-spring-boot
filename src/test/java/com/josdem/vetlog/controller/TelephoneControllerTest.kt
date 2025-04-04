package com.josdem.vetlog.controller

import org.slf4j.LoggerFactory
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
class TelephoneControllerTest {

    @Autowired
    private lateinit var mockMvc : MockMvc

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var webApplicationContext : WebApplicationContext

    private val image = MockMultipartFile(
        "mockImage",
        "image.jpg",
        "image/jpeg",
        "image".toByteArray()
    )

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply { springSecurity() }
            .build()
    }

    @Test
    @Transactional
    @DisplayName("showing adopting form")
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun shouldShowAdoptingForm(testInfo : TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        registerPet()
        val request = get("/telephone/adopt")
            .param("uuid", PET_UUID)
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("telephone/adopt"))
            .andExpect(model().attributeExists("pet"))
            .andExpect(model().attributeExists("telephoneCommand"))
            .andExpect(status().isOk())
    }


    @Test
    @Transactional
    @DisplayName("not saving adoption due to invalid phone number")
    @WithMockUser(username = "josdem", password = "12345678", roles = ["USER"])
    fun shouldNotSaveAdoption(testInfo : TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        registerPet()
        val request = post("/telephone/save")
            .with(csrf())
            .param("uuid", PET_UUID)
            .param("mobile", "123")
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("telephone/adopt"))
            .andExpect(model().attributeExists("pet"))
            .andExpect(model().attributeExists("telephoneCommand"))
            .andExpect(status().isOk())
    }

    private fun registerPet() {
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
