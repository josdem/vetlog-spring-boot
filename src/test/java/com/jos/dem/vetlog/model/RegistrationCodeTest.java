package com.jos.dem.vetlog.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class RegistrationCodeTest {

  private RegistrationCode registrationCode = new RegistrationCode();

  @Test
  @DisplayName("getting a valid registration code")
  void shouldGetValidRegistrationCode(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    assertEquals(7, DAYS.between(registrationCode.getDateCreated(), LocalDate.now().plusDays(7)));
    assertEquals(36, registrationCode.getToken().length());
    assertTrue(registrationCode.isValid());
  }
}
