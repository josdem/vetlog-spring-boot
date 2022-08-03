/*
Copyright 2022 Jose Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.service.impl;

import com.jos.dem.vetlog.command.ChangePasswordCommand;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.MessageCommand;
import com.jos.dem.vetlog.command.RegistrationCommand;
import com.jos.dem.vetlog.exception.UserNotFoundException;
import com.jos.dem.vetlog.exception.VetlogException;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.RegistrationCodeRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.RecoveryService;
import com.jos.dem.vetlog.service.RegistrationService;
import com.jos.dem.vetlog.service.RestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecoveryServiceImpl implements RecoveryService {

    private final RestService restService;
    private final RegistrationService registrationService;
    private final UserRepository userRepository;
    private final RegistrationCodeRepository repository;
    private final LocaleService localeService;

    @Value("${baseUrl}")
    private String baseUrl;
    @Value("${token}")
    private String clientToken;
    @Value("${template.register.name}")
    private String registerTemplate;
    @Value("${template.register.path}")
    private String registerPath;
    @Value("${template.forgot.name}")
    private String forgotTemplate;
    @Value("${template.forgot.path}")
    private String forgotPath;


    public void sendConfirmationAccountToken(String email) {
        String token = registrationService.generateToken(email);
        RegistrationCommand command = new RegistrationCommand();
        command.setName(email);
        command.setEmail(email);
        command.setTemplate(registerTemplate);
        command.setMessage(baseUrl + registerPath + token);
        command.setToken(clientToken);
        log.info("Registration command: {}", command);
        restService.sendMessage(command);
    }

    public User confirmAccountForToken(String token) {
        User user = getUserByToken(token);
        if (user == null) {
            throw new UserNotFoundException(localeService.getMessage("exception.user.not.found"));
        }
        user.setEnabled(true);
        userRepository.save(user);
        return user;
    }

    public User getUserByToken(String token) {
        String email = registrationService.findEmailByToken(token);
        if (email == null) {
            throw new VetlogException(localeService.getMessage("exception.token.not.found"));
        }
        User user = userRepository.findByEmail(email);
        return user;
    }

    public void generateRegistrationCodeForEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException(localeService.getMessage("exception.user.not.found"));
        }
        if (user.getEnabled() == false) {
            throw new VetlogException(localeService.getMessage("exception.account.not.activated"));
        }
        String token = registrationService.generateToken(email);
        MessageCommand command = new MessageCommand();
        command.setEmail(email);
        command.setTemplate(forgotTemplate);
        command.setUrl(baseUrl + forgotPath + token);
        restService.sendMessage(command);
    }

    public Boolean validateToken(String token) {
        return repository.findByToken(token) != null;
    }

    public User changePassword(Command command) {
        ChangePasswordCommand changePasswordCommand = (ChangePasswordCommand) command;
        User user = getUserByToken(changePasswordCommand.getToken());
        user.setPassword(new BCryptPasswordEncoder().encode(changePasswordCommand.getPassword()));
        userRepository.save(user);
        return user;
    }

}
