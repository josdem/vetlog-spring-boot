package com.josdem.vetlog.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class VaccineFormatterTest {

    @InjectMocks
    private final VaccineFormatter vaccineFormatter = new VaccineFormatter();

    @Mock
    private Locale locale;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("formatting DA2PP if locale is Spanish")
    void shouldFormatDA2PPInSpanish(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        when(locale.getLanguage()).thenReturn("es");

        assertEquals("Quintuple Canina", vaccineFormatter.format("DA2PP", locale));
    }

    @Test
    @DisplayName("formatting deworming if locale is Spanish")
    void shouldFormatDewormingInSpanish(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        when(locale.getLanguage()).thenReturn("es");

        assertEquals("Desparasitación", vaccineFormatter.format("Deworming", locale));
    }

    @Test
    @DisplayName("formatting rabies if locale is Spanish")
    void shouldFormatRabiesInSpanish(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        when(locale.getLanguage()).thenReturn("es");

        assertEquals("Rabia", vaccineFormatter.format("Rabies", locale));
    }
}
