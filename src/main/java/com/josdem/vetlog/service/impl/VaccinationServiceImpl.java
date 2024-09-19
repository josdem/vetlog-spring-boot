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
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.service.VaccinationService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class VaccinationServiceImpl implements VaccinationService {

    @Override
    public void save(Pet pet) {
        if (pet.getBreed().getType() != PetType.DOG) {
            throw new BusinessException("Only dogs are allowed");
        }
        var weeks = ChronoUnit.WEEKS.between(pet.getBirthDate(), LocalDate.now());
    }
}
