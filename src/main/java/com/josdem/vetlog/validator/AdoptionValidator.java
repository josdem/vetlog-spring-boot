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

package com.josdem.vetlog.validator;

import com.josdem.vetlog.command.AdoptionCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AdoptionValidator implements Validator {

    private static final String REGEX = "[a-f0-9]{8}(?:-[a-f0-9]{4}){4}[a-f0-9]{8}";

    @Override
    public boolean supports(Class<?> clazz) {
        return AdoptionCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AdoptionCommand adoptionCommand = (AdoptionCommand) target;
        if (!adoptionCommand.getUuid().matches(REGEX)) {
            errors.rejectValue("uuid", "adoption.error.uuid.invalid");
        }
    }
}
