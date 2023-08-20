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

package com.jos.dem.vetlog.config;

import com.jos.dem.vetlog.enums.CurrentEnvironment;
import com.jos.dem.vetlog.enums.Role;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.BreedRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements ApplicationListener<ApplicationReadyEvent> {

  @Autowired private Environment environment;
  @Autowired private UserRepository userRepository;
  @Autowired private BreedRepository breedRepository;

  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {
    if (environment.getActiveProfiles()[0].equals(
        CurrentEnvironment.DEVELOPMENT.getDescription())) {
      log.info("Loading development environment");
      createDefaultUsers();
    }
  }

  void createDefaultUsers() {
    createUserWithRole("josdem", "12345678", "joseluis.delacruz@gmail.com", Role.USER);
    createUserWithRole("miriam", "12345678", "miriam@gmail.com", Role.USER);
    createUserWithRole("admin", "12345678", "admin@email.com", Role.ADMIN);
  }

  void createUserWithRole(String username, String password, String email, Role authority) {
    if (userRepository.findByUsername(username) == null) {
      User user = new User();
      user.setUsername(username);
      user.setPassword(new BCryptPasswordEncoder().encode(password));
      user.setEmail(email);
      user.setRole(authority);
      user.setFirstName(username);
      user.setLastName(username);
      user.setEnabled(true);
      userRepository.save(user);
    }
  }
}
