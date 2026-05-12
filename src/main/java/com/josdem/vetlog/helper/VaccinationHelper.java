/*
  Copyright 2026 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.helper;

import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.VaccinationRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VaccinationHelper {

    private static final String RABIES_VACCINE = "Rabies";

    private final VaccinationRepository vaccinationRepository;

    public void validateRabiesVaccine(List<Vaccination> previousVaccines, List<Vaccination> newVaccines, Pet pet) {
        for (Vaccination newVaccine : newVaccines) {
            if (RABIES_VACCINE.equalsIgnoreCase(newVaccine.getName())
                    && newVaccine.getStatus() == VaccinationStatus.APPLIED) {
                previousVaccines.stream()
                        .filter(v -> RABIES_VACCINE.equalsIgnoreCase(v.getName()))
                        .findFirst()
                        .ifPresent(oldVaccine -> {
                            if (oldVaccine.getStatus() == VaccinationStatus.PENDING) {
                                // Create a new Rabies vaccine for one year later
                                Vaccination futureRabies = new Vaccination(
                                        null, RABIES_VACCINE, LocalDate.now().plusYears(1), VaccinationStatus.NEW, pet);
                                vaccinationRepository.save(futureRabies);
                            }
                        });
            }
        }
    }
}
