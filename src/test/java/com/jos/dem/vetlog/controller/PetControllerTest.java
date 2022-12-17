/*
Copyright 2022 Jose Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("showing create pet form")
  @WithMockUser(username = "josdem", password = "12345678", roles = "USER")
  void shouldShowCreatePetForm(TestInfo testInfo) throws Exception {
    log.info("Running: {}", testInfo.getDisplayName());
    mockMvc
        .perform(get("/pet/create"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("petCommand"))
        .andExpect(model().attributeExists("breeds"))
        .andExpect(model().attributeExists("breedsByTypeUrl"))
        .andExpect(view().name("pet/create"));
  }

  @Test
  @DisplayName("showing edit pet form")
  @WithMockUser(username = "josdem", password = "12345678", roles = "USER")
  void shouldShowEditPetForm(TestInfo testInfo) throws Exception {
    log.info("Running: {}", testInfo.getDisplayName());
    mockMvc
        .perform(get("/pet/edit").param("uuid", "43443f72-68d5-473a-9158-729f0b9f70bb"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("petCommand"))
        .andExpect(model().attributeExists("breeds"))
        .andExpect(model().attributeExists("breedsByTypeUrl"))
        .andExpect(view().name("pet/edit"));
  }
}
