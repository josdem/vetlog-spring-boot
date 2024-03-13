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

import com.jos.dem.vetlog.exception.VetlogException;
import com.jos.dem.vetlog.model.RegistrationCode;
import com.jos.dem.vetlog.repository.RegistrationCodeRepository;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.RegistrationService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final LocaleService localeService;
    private final RegistrationCodeRepository repository;

    public Optional<String> findEmailByToken(String token) {
        RegistrationCode registrationCode = repository
                .findByToken(token)
                .orElseThrow(() -> new VetlogException(localeService.getMessage("exception.token.not.found")));

        return registrationCode.getEmail().describeConstable();
    }

    public String generateToken(String email) {
        RegistrationCode registrationCode = new RegistrationCode();
        registrationCode.setEmail(email);
        repository.save(registrationCode);
        return registrationCode.getToken();
    }
}
