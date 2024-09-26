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

package com.josdem.vetlog.binder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.josdem.vetlog.command.UserCommand;
import com.josdem.vetlog.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@Slf4j
class UserBinderTest {

    private final UserBinder binder = new UserBinder();

    @Test
    @DisplayName("binding an user")
    void shouldBindAnUser(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        var userCommand = getUserCommand();

        var result = binder.bindUser(userCommand);

        assertEquals("josdem", result.getUsername());
        assertEquals(60, result.getPassword().length());
        assertEquals("Jose", result.getFirstName());
        assertEquals("Morales", result.getLastName());
        assertEquals("1234567890", result.getMobile());
        assertEquals("+52", result.getCountryCode());
        assertEquals("contact@josdem.io", result.getEmail());
        assertEquals(Role.USER, result.getRole());
    }

    @NotNull
    private UserCommand getUserCommand() {
        var userCommand = new UserCommand();
        userCommand.setUsername("josdem");
        userCommand.setPassword("password");
        userCommand.setPasswordConfirmation("password");
        userCommand.setFirstname("Jose");
        userCommand.setLastname("Morales");
        userCommand.setCountryCode("+52");
        userCommand.setMobile("1234567890");
        userCommand.setEmail("contact@josdem.io");
        return userCommand;
    }
}
