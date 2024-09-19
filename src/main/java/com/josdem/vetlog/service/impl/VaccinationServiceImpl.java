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

package com.josdem.vetlog.service.impl;

import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.VaccinationRepository;
import com.josdem.vetlog.service.VaccinationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VaccinationServiceImpl implements VaccinationService {

    private final VaccinationRepository vaccinationRepository;

    @Override
    public void save(Pet pet) {
        if (pet.getBreed().getType() != PetType.DOG) {
            throw new BusinessException("Only dogs are allowed");
        }

        Long weeks = ChronoUnit.WEEKS.between(pet.getBirthDate(), LocalDateTime.now());

        switch (weeks.intValue()) {
            case 0, 1, 2, 3, 4, 5 -> log.info("No vaccination needed");
            case 6, 7, 8, 9 -> {
                log.info("First vaccination");
                registerVaccination("DA2PP");
                registerVaccination("Deworming");
            }
            case 10, 11, 12, 13 -> {
                log.info("Second vaccination");
                registerVaccination("DA2PP");
                registerVaccination("Deworming");
                registerVaccination("Leptospirosis");
            }
            case 14, 15, 16 -> {
                log.info("Third vaccination");
                registerVaccination("DA2PP");
                registerVaccination("Deworming");
                registerVaccination("Leptospirosis");
                registerVaccination("Rabies");
            }
            default -> {
                log.info("Annual vaccination");
                registerVaccination("DA2PP");
                registerVaccination("Deworming");
                registerVaccination("Leptospirosis");
                registerVaccination("Rabies");
                registerVaccination("Canine influenza");
            }
        }
    }

    private void registerVaccination(String name) {
        vaccinationRepository.save(new Vaccination(null, name, LocalDate.now(), VaccinationStatus.PENDING));
    }
}
