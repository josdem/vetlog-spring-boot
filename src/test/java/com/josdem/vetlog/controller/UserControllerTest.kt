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

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.model
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc : MockMvc

    private val log = LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var webApplicationContext : WebApplicationContext

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply { springSecurity() }
            .build()
    }

    @Test
    @Transactional
    @DisplayName("registering an user")
    fun shouldShowCreateUserForm(testInfo : TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val request = post("/user/save")
            .with(csrf())
            .param("username", "vetlog")
            .param("password", "12345678")
            .param("passwordConfirmation", "12345678")
            .param("firstname", "vetlog")
            .param("lastname", "organization")
            .param("countryCode", "+52")
            .param("mobile", "1234567890")
            .param("email", "contact@josdem.io")
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("login/login"))
    }

    @Test
    @DisplayName("not saving user due to invalid email")
    fun shouldNotSaveUserWithInvalidEmail(testInfo : TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val request = post("/user/save")
            .with(csrf())
            .param("username", "vetlog")
            .param("password", "12345678")
            .param("passwordConfirmation", "12345678")
            .param("firstname", "vetlog")
            .param("lastname", "organization")
            .param("countryCode", "+52")
            .param("mobile", "1234567890")
            .param("email", "contact")
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("user/create"))
            .andExpect(model().attributeHasFieldErrors("userCommand", "email"))
    }

    @Test
    @DisplayName("not saving user due to invalid mobile")
    fun shouldNotSaveUserWithInvalidMobile(testInfo : TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val request = post("/user/save")
            .with(csrf())
            .param("username", "vetlog")
            .param("password", "12345678")
            .param("passwordConfirmation", "12345678")
            .param("firstname", "vetlog")
            .param("lastname", "organization")
            .param("countryCode", "+52")
            .param("mobile", "notValidMobile")
            .param("email", "contact")
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("user/create"))
            .andExpect(model().attributeHasFieldErrors("userCommand", "mobile"))
    }
}
