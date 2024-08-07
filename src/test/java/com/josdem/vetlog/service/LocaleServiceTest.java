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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.helper.LocaleResolver;
import com.josdem.vetlog.service.impl.LocaleServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

@Slf4j
class LocaleServiceTest {

    private LocaleService service;

    @Mock
    private MessageSource messageSource;

    @Mock
    private LocaleResolver localeResolver;

    private String code = "code";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new LocaleServiceImpl(messageSource, localeResolver);
    }

    @Test
    @DisplayName("getting message by request")
    void shouldGetMessageByRequest(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(messageSource.getMessage(code, null, localeResolver.resolveLocale(request)))
                .thenReturn("message");
        assertEquals("message", service.getMessage(code, request));
    }

    @Test
    @DisplayName("getting message by code")
    void shouldGetMessageByCode(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(messageSource.getMessage(code, null, new Locale("en"))).thenReturn("message");
        assertEquals("message", service.getMessage(code));
    }
}
