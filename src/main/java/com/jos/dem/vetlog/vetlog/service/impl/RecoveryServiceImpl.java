/*
Copyright  2024 Jose Morales contact@josdem.io

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
import com.jos.dem.vetlog.exception.BusinessException;
import com.jos.dem.vetlog.exception.UserNotFoundException;
import com.jos.dem.vetlog.exception.VetlogException;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.RegistrationCodeRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.RecoveryService;
import com.jos.dem.vetlog.service.RegistrationService;
import com.jos.dem.vetlog.service.RestService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        String email = registrationService
                .findEmailByToken(token)
                .orElseThrow(() -> new VetlogException(localeService.getMessage("exception.token.not.found")));
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(localeService.getMessage("exception.user.not.found")));
    }

    public void generateRegistrationCodeForEmail(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(localeService.getMessage("exception.user.not.found")));
        if (user.getEnabled() == false) {
            throw new VetlogException(localeService.getMessage("exception.account.not.activated"));
        }
        try {
            String token = registrationService.generateToken(email);
            MessageCommand command = new MessageCommand();
            command.setEmail(email);
            command.setName(email);
            command.setTemplate(forgotTemplate);
            command.setMessage(baseUrl + forgotPath + token);
            command.setToken(clientToken);
            restService.sendMessage(command);
        } catch (IOException ioException) {
            throw new BusinessException(ioException.getMessage());
        }
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
