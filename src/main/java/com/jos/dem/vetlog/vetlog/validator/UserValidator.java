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

package com.jos.dem.vetlog.validator;

import com.jos.dem.vetlog.command.UserCommand;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCommand userCommand = (UserCommand) target;
        validatePasswords(errors, userCommand);
        validateUsername(errors, userCommand);
        validateEmail(errors, userCommand);
    }

    private void validatePasswords(Errors errors, UserCommand command) {
        if (!command.getPassword().equals(command.getPasswordConfirmation())) {
            errors.rejectValue("password", "user.error.password.equals");
        }
    }

    private void validateUsername(Errors errors, UserCommand command) {
        Optional<User> optional = userRepository.findByUsername(command.getUsername());
        if (optional.isPresent()) {
            errors.rejectValue("username", "user.error.duplicated.username");
        }
    }

    public void validateEmail(Errors errors, UserCommand command) {
        Optional<User> optional = userRepository.findByEmail(command.getEmail());
        if (optional.isPresent()) {
            errors.rejectValue("email", "user.error.duplicated.email");
        }
    }
}
