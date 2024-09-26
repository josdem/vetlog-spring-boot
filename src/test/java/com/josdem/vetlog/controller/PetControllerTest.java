/*
Copyright 2024 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.repository.PetRepository;
import com.josdem.vetlog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    public static final String PET_UUID = "5acd03eb-2795-4e92-85d0-92e142c44170";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    private final MockMultipartFile image =
            new MockMultipartFile("mockImage", "image.jpg", "image/jpeg", "image".getBytes());

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @Transactional
    @DisplayName("registering a pet")
    @WithMockUser(username = "josdem", password = "12345678", roles = "USER")
    void shouldRegisterNewPet(TestInfo testInfo) throws Exception {
        log.info("Running: {}", testInfo.getDisplayName());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/pet/save")
                        .file(image)
                        .with(csrf())
                        .param("name", "Cremita")
                        .param("uuid", PET_UUID)
                        .param("birthDate", "2024-08-22T09:28:00")
                        .param("dewormed", "true")
                        .param("vaccinated", "true")
                        .param("sterilized", "true")
                        .param("breed", "11")
                        .param("user", "1")
                        .param("status", PetStatus.IN_ADOPTION.toString())
                        .param("type", PetType.DOG.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("pet/create"));
    }

    @Test
    @Transactional
    @DisplayName("showing edit pet form")
    @WithMockUser(username = "josdem", password = "12345678", roles = "USER")
    void shouldShowEditPetForm(TestInfo testInfo) throws Exception {
        log.info("Running: {}", testInfo.getDisplayName());
        // Set up data before the test
        mockMvc.perform(MockMvcRequestBuilders.multipart("/pet/save")
                        .file(image)
                        .with(csrf())
                        .param("name", "Cremita")
                        .param("uuid", PET_UUID)
                        .param("birthDate", "2024-08-22T09:28:00")
                        .param("dewormed", "true")
                        .param("vaccinated", "true")
                        .param("sterilized", "true")
                        .param("breed", "11")
                        .param("user", "1")
                        .param("status", PetStatus.IN_ADOPTION.toString())
                        .param("type", PetType.DOG.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("pet/create"));

        // Edit test
        mockMvc.perform(get("/pet/edit").param("uuid", PET_UUID))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("petCommand"))
                .andExpect(model().attributeExists("breeds"))
                .andExpect(model().attributeExists("breedsByTypeUrl"))
                .andExpect(view().name("pet/edit"));
    }

    @Test
    @Transactional
    @DisplayName("updating pet status")
    @WithMockUser(username = "josdem", password = "12345678", roles = "USER")
    void shouldUpdatePet(TestInfo testInfo) throws Exception {
        log.info("Running: {}", testInfo.getDisplayName());
        // Set up data before the test
        mockMvc.perform(MockMvcRequestBuilders.multipart("/pet/save")
                        .file(image)
                        .with(csrf())
                        .param("name", "Cremita")
                        .param("uuid", PET_UUID)
                        .param("birthDate", "2024-08-22T09:28:00")
                        .param("dewormed", "true")
                        .param("vaccinated", "true")
                        .param("sterilized", "true")
                        .param("breed", "11")
                        .param("user", "1")
                        .param("status", PetStatus.IN_ADOPTION.toString())
                        .param("type", PetType.DOG.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("pet/create"));

        var user = userRepository.findByUsername("josdem").orElseThrow(() -> new RuntimeException("User not found"));
        var cremita = petRepository.findByUuid(PET_UUID).orElseThrow(() -> new RuntimeException("Pet not found"));

        // Update test
        mockMvc.perform(MockMvcRequestBuilders.multipart("/pet/update")
                        .file(image)
                        .with(csrf())
                        .param("id", cremita.getId().toString())
                        .param("name", "Cremita")
                        .param("uuid", PET_UUID)
                        .param("birthDate", "2024-08-22T09:28:00")
                        .param("dewormed", "true")
                        .param("vaccinated", "true")
                        .param("sterilized", "true")
                        .param("breed", "11")
                        .param("user", user.getId().toString())
                        .param("status", PetStatus.OWNED.toString())
                        .param("type", PetType.DOG.toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("message"))
                .andExpect(view().name("pet/edit"));
    }
}
