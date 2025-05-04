package com.josdem.vetlog.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.context.TestPropertySource

@WebMvcTest(GoogleMapController::class)
@TestPropertySource(properties = ["google.maps.api.key=mocked-api-key"])
class GoogleMapControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `showMap should return Map view with API key`() {
        mockMvc.perform(get("/map"))
            .andExpect(status().isOk)
            .andExpect(view().name("map/map"))
            .andExpect(model().attributeExists("apiKey"))
            .andExpect(model().attributeExists("apiKey","mocked-api-key"))
    }
}
