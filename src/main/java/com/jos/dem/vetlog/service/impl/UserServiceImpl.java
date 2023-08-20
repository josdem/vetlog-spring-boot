/*
Copyright 2023 Jose Morales joseluis.delacruz@gmail.com

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

import com.jos.dem.vetlog.binder.UserBinder;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.RecoveryService;
import com.jos.dem.vetlog.service.UserService;
import com.jos.dem.vetlog.util.UserContextHolderProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserBinder userBinder;
    private final UserRepository userRepository;
    private final RecoveryService recoveryService;
    private final UserContextHolderProvider provider;

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User save(Command command) {
        User user = userBinder.bindUser(command);
        userRepository.save(user);
        return user;
    }

    public User getCurrentUser() {
        Authentication auth = provider.getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).get();
    }

}
