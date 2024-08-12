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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.binder.UserBinder;
import com.josdem.vetlog.command.Command;
import com.josdem.vetlog.model.User;
import com.josdem.vetlog.repository.UserRepository;
import com.josdem.vetlog.service.impl.UserServiceImpl;
import com.josdem.vetlog.util.UserContextHolderProvider;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

@Slf4j
class UserServiceTest {

    private static final String USERNAME = "josdem";
    private static final String EMAIL = "contact@josdem.io";
    private UserService service;
    private User user = new User();

    @Mock
    private UserBinder userBinder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserContextHolderProvider provider;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new UserServiceImpl(userBinder, userRepository, provider);
    }

    @Test
    @DisplayName("getting user by username")
    void shouldGetUserByUsername(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.ofNullable(user));
        User result = service.getByUsername(USERNAME);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("getting user by email")
    void shouldGetUserByEmail(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.ofNullable(user));
        User result = service.getByEmail(EMAIL);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("saving an user")
    void shouldSaveAnUser(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        Command command = mock(Command.class);
        final User user = new User();
        user.setEmail(EMAIL);
        when(userBinder.bindUser(command)).thenReturn(user);

        final User result = service.save(command);

        verify(userRepository).save(user);
        assertEquals(user, result);
    }

    @Test
    @DisplayName("getting current user")
    void shouldGetCurrentUser(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(USERNAME);
        when(provider.getAuthentication()).thenReturn(authentication);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.ofNullable(user));
        assertEquals(user, service.getCurrentUser());
    }
}
