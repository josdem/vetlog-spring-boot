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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.enums.Role;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.User;
import com.josdem.vetlog.repository.UserRepository;
import com.josdem.vetlog.service.impl.UserDetailsServiceImpl;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
class UserDetailsServiceTest {

    private static final String USERNAME = "josdem";

    private UserDetailsServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    @DisplayName("loading user by username")
    void shouldLoadUserByUsername(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword("password");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRole(Role.USER);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername(USERNAME);

        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getEnabled(), result.isEnabled());
    }

    @Test
    @DisplayName("not search for authorities since user does not exist")
    void shouldNotSearchForAuthoritiesDueToUserNotFound(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        assertThrows(BusinessException.class, () -> service.loadUserByUsername("thisUserDoesNotExist"));
    }
}
