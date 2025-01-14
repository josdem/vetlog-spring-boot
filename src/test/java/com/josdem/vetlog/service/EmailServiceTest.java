/*
  Copyright 2025 Jose Morales contact@josdem.io

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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.command.MessageCommand;
import com.josdem.vetlog.config.TemplateProperties;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.User;
import java.io.IOException;

import com.josdem.vetlog.service.impl.EmailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class EmailServiceTest {

    private EmailService emailService;

    @Mock
    private RestService restService;

    @Mock
    private LocaleService localeService;

    @Mock
    private TemplateProperties templateProperties;

    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl(restService, localeService, templateProperties);
        user = new User();
    }

    @Test
    @DisplayName("sending a welcome email")
    void shouldSendWelcomeEmail(TestInfo testInfo) throws Exception {
        log.info(testInfo.getDisplayName());
        when(templateProperties.getWelcome()).thenReturn("welcome.ftl");
        user.setFirstName("Jose");
        user.setEmail("contact@josdem.io");

        emailService.sendWelcomeEmail(user);

        verify(localeService.getMessage("user.welcome.message"));
        verify(restService).sendMessage(isA(MessageCommand.class));
    }

    @Test
    @DisplayName("not sending a welcome email due to an exception")
    void shouldNotSendWelcomeEmail(TestInfo testInfo) throws Exception {
        log.info(testInfo.getDisplayName());
        when(restService.sendMessage(isA(MessageCommand.class))).thenThrow(new IOException("Error"));

        assertThrows(BusinessException.class, () -> emailService.sendWelcomeEmail(user));
    }
}
