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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.command.ChangePasswordCommand;
import com.josdem.vetlog.command.MessageCommand;
import com.josdem.vetlog.exception.UserNotFoundException;
import com.josdem.vetlog.exception.VetlogException;
import com.josdem.vetlog.model.RegistrationCode;
import com.josdem.vetlog.model.User;
import com.josdem.vetlog.repository.RegistrationCodeRepository;
import com.josdem.vetlog.repository.UserRepository;
import com.josdem.vetlog.service.impl.RecoveryServiceImpl;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class RecoveryServiceTest {

    private static final String TOKEN = "token";
    private static final String EMAIL = "contact@josdem.io";
    private RecoveryService service;

    private final User user = new User();

    @Mock
    private RestService restService;

    @Mock
    private RegistrationService registrationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationCodeRepository repository;

    @Mock
    private LocaleService localeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new RecoveryServiceImpl(restService, registrationService, userRepository, repository, localeService);
    }

    @Test
    @DisplayName("sending activation account token")
    void shouldSendActivationAccountToken(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());

        when(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));

        service.confirmAccountForToken(TOKEN);

        assertTrue(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("not sending activation account token due to token do not exist")
    void shouldNotSendActivationAccountTokenDueNotValidToken(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        assertThrows(VetlogException.class, () -> service.confirmAccountForToken(TOKEN));
    }

    @Test
    @DisplayName("not sending activation account token due to user not found")
    void shouldNotSendActivationAccountTokenDueUserNotFound(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        when(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL));
        assertThrows(UserNotFoundException.class, () -> service.confirmAccountForToken(TOKEN));
    }

    @Test
    @DisplayName("generating token to change password")
    void shouldSendChangePasswordToken(TestInfo testInfo) throws IOException {
        log.info(testInfo.getDisplayName());

        user.setEnabled(true);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        when(registrationService.generateToken(EMAIL)).thenReturn(TOKEN);

        service.generateRegistrationCodeForEmail(EMAIL);
        verify(restService).sendMessage(isA(MessageCommand.class));
    }

    @Test
    @DisplayName("not sending change password token due to user not found")
    void shouldNotSendChangePasswordTokenDueUserNotFound(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        assertThrows(UserNotFoundException.class, () -> service.generateRegistrationCodeForEmail(EMAIL));
    }

    @Test
    @DisplayName("not sending change password token due to user not enabled")
    void shouldNotSendChangePasswordTokenDueUserNotEnabled(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        user.setEnabled(false);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        assertThrows(VetlogException.class, () -> service.generateRegistrationCodeForEmail(EMAIL));
    }

    @Test
    @DisplayName("validating a token")
    void shouldValidateToken(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        when(repository.findByToken(TOKEN)).thenReturn(Optional.of(new RegistrationCode()));
        assertTrue(service.validateToken(TOKEN));
    }

    @Test
    @DisplayName("finding an invalid token")
    void shouldFindInvalidToken(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        assertFalse(service.validateToken(TOKEN));
    }

    @Test
    @DisplayName("changing password")
    void shouldChangePassword(TestInfo testInfo) {
        log.info(testInfo.getDisplayName());
        var changePasswordCommand = new ChangePasswordCommand();
        changePasswordCommand.setToken(TOKEN);
        changePasswordCommand.setPassword("password");
        when(registrationService.findEmailByToken(TOKEN)).thenReturn(Optional.of(EMAIL));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        service.changePassword(changePasswordCommand);

        assertEquals(60, user.getPassword().length());
        verify(userRepository).save(user);
    }
}
