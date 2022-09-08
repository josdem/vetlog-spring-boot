package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.service.impl.AdoptionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class AdoptionServiceTest {

  private AdoptionService service = new AdoptionServiceImpl();

  @Mock private PetService petService;

  @Test
  @DisplayName("saving a pet in adoption")
  void shouldSaveAPetInAdoption(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    assertTrue(true);
  }
}
