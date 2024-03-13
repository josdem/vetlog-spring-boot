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

import com.jos.dem.vetlog.command.TelephoneCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TelephoneValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TelephoneCommand.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TelephoneCommand telephoneCommand = (TelephoneCommand) target;
    }
}
