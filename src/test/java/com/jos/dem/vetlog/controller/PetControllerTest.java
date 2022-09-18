package com.jos.dem.vetlog.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
  @DisplayName("listing pets for adoption")
  void shouldListPetsForAdoption(TestInfo testInfo) throws Exception {
    log.info("Running: {}", testInfo.getDisplayName());
    mockMvc
        .perform(get("/pet/listForAdoption"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("petListEmpty"))
        .andExpect(view().name("pet/listForAdoption"));
  }
}
