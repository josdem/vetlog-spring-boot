package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.model.RegistrationCode;
import com.jos.dem.vetlog.repository.RegistrationCodeRepository;
import com.jos.dem.vetlog.service.impl.RegistrationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
class RegistrationServiceTest {

    private static final String TOKEN = "token";
    private static final String EMAIL = "contact@josdem.io";
    private RegistrationService service;

    @Mock
    private LocaleService localeService;
    @Mock
    private RegistrationCodeRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new RegistrationServiceImpl(localeService, repository);
    }

    @Test
    @DisplayName("getting user by token")
    void shouldGetUserByToken(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        RegistrationCode registrationCode = new RegistrationCode();
        registrationCode.setEmail(EMAIL);
        when(repository.findByToken(TOKEN)).thenReturn(registrationCode);
        assertEquals(EMAIL, service.findEmailByToken(TOKEN));
    }
}
