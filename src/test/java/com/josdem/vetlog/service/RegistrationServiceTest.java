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

package com.josdem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.model.RegistrationCode;
import com.josdem.vetlog.repository.RegistrationCodeRepository;
import com.josdem.vetlog.service.impl.RegistrationServiceImpl;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        var registrationCode = new RegistrationCode();
        registrationCode.setEmail(EMAIL);
        when(repository.findByToken(TOKEN)).thenReturn(Optional.of(registrationCode));
        assertEquals(EMAIL, service.findEmailByToken(TOKEN).get());
    }

    @Test
    @DisplayName("generating token")
    void shouldGenerateToken(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        var token = service.generateToken(EMAIL);
        assertEquals(36, token.length());
        verify(repository).save(isA(RegistrationCode.class));
    }
}
