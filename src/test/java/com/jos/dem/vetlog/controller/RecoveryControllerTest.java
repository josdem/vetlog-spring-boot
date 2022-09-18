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
class RecoveryControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("validating token")
  void shouldValidateToken(TestInfo testInfo) throws Exception {
    log.info("Running: {}", testInfo.getDisplayName());
    mockMvc
        .perform(get("/recovery/activate/token"))
        .andExpect(status().isOk())
        .andExpect(view().name("error"));
  }

  @Test
  @DisplayName("getting email to change password")
  void shouldRequestEmailToChangePassword(TestInfo testInfo) throws Exception {
    log.info("Running: {}", testInfo.getDisplayName());
    mockMvc
        .perform(get("/recovery/password"))
        .andExpect(status().isOk())
        .andExpect(view().name("recovery/recoveryPassword"));
  }

  @Test
  @DisplayName("showing change password forms")
  void shouldShowChangePasswordForms(TestInfo testInfo) throws Exception {
    log.info("Running: {}", testInfo.getDisplayName());
    mockMvc
        .perform(get("/recovery/forgot/token"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("message"))
        .andExpect(view().name("recovery/changePassword"));
  }
}
