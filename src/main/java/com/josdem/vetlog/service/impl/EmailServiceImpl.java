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

package com.josdem.vetlog.service.impl;

import com.josdem.vetlog.command.MessageCommand;
import com.josdem.vetlog.config.TemplateProperties;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.exception.UserNotFoundException;
import com.josdem.vetlog.model.User;
import com.josdem.vetlog.service.*;
import com.josdem.vetlog.util.TemplateLocaleResolver;
import java.io.IOException;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(TemplateProperties.class)
public class EmailServiceImpl implements EmailService {

    @Value("${token}")
    private String clientToken;

    private final RestService restService;
    private final LocaleService localeService;
    private final TemplateProperties templateProperties;
    private final PetService petService;

    public void sendWelcomeEmail(User user, Locale locale) {
        log.info("Sending welcome email to: {}", user.getFirstName());
        if (!user.isEnabled()) {
            return;
        }
        var template = TemplateLocaleResolver.getTemplate(templateProperties.getWelcome(), locale.getLanguage());
        try {
            var command = new MessageCommand();
            command.setEmail(user.getEmail());
            command.setName(user.getFirstName());
            command.setTemplate(template);
            command.setSubject(localeService.getMessage("email.subject", locale));
            command.setMessage(localeService.getMessage("user.welcome.message", locale));
            command.setToken(clientToken);
            restService.sendMessage(command);
        } catch (IOException ioe) {
            throw new BusinessException(ioe.getMessage());
        }
    }

    @Override
    public void sendPullingUpEmail(Long petId, Locale locale) {
        var pet = petService.getPetById(petId);
        var user = pet.getUser();
        if (user == null) {
            throw new UserNotFoundException("No user was found for pet with id: " + petId);
        }
        log.info("Sending pulling up email to: {}", user.getFirstName());
        if (!user.isEnabled()) {
            return;
        }
        var template = TemplateLocaleResolver.getTemplate(templateProperties.getPullingUp(), locale.getLanguage());
        try {
            var command = new MessageCommand();
            command.setEmail(user.getEmail());
            command.setName(user.getFirstName());
            command.setTemplate(template);
            command.setSubject(localeService.getMessage("email.subject", locale));
            command.setMessage(pet.getName());
            command.setToken(clientToken);
            restService.sendMessage(command);
        } catch (IOException ioe) {
            throw new BusinessException(ioe.getMessage());
        }
    }
}
