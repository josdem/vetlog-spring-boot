package com.josdem.vetlog.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@Slf4j
class VaccinationStatusTest {

    private final VaccinationStatus vaccinationStatus = VaccinationStatus.PENDING;

    @Test
    @DisplayName("returning vaccination status")
    void shouldReturnVaccinationStatus(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        assertEquals("PENDING", vaccinationStatus.name());
        assertEquals("Pending", vaccinationStatus.getValue());
    }
}
