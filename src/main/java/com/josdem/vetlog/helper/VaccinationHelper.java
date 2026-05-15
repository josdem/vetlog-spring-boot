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
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VaccinationHelper {

    private static final String RABIES_VACCINE = "Rabies";
    private static final String PUPPY_VACCINE = "Puppy";
    private static final String C4CV_VACCINE = "C4CV";
    private static final String C6CV_VACCINE = "C6CV";

    private final VaccinationRepository vaccinationRepository;

    public void validateRabiesVaccine(List<Vaccination> previousVaccines, List<Vaccination> newVaccines, Pet pet) {
        Set<String> validationVaccines = Set.of(C6CV_VACCINE.toUpperCase(), RABIES_VACCINE.toUpperCase());
        Map<String, Integer> daysAfterVaccineApplied = Map.of(
                C6CV_VACCINE.toUpperCase(), 15,
                /* Create new rabies vaccine after one year */
                RABIES_VACCINE.toUpperCase(), 365);
        for (Vaccination newVaccine : newVaccines) {
            if (validationVaccines.contains(newVaccine.getName().toUpperCase())
                    && newVaccine.getStatus() == VaccinationStatus.APPLIED) {
                previousVaccines.stream()
                        .filter(v -> validationVaccines.contains(v.getName().toUpperCase()))
                        .findFirst()
                        .ifPresent(oldVaccine -> {
                            if (oldVaccine.getStatus() == VaccinationStatus.PENDING) {

                                int days = daysAfterVaccineApplied.get(
                                        oldVaccine.getName().toUpperCase());
                                Vaccination futureRabies = new Vaccination(
                                        null,
                                        RABIES_VACCINE,
                                        LocalDate.now().plusDays(days),
                                        VaccinationStatus.NEW,
                                        pet);
                                vaccinationRepository.save(futureRabies);
                            }
                        });
            }
        }
    }

    public void validatePuppyVaccines(List<Vaccination> previousVaccines, List<Vaccination> newVaccines, Pet pet) {
        for (Vaccination newVaccine : newVaccines) {
            if (PUPPY_VACCINE.equalsIgnoreCase(newVaccine.getName())
                    && newVaccine.getStatus() == VaccinationStatus.APPLIED) {
                previousVaccines.stream()
                        .filter(v -> PUPPY_VACCINE.equalsIgnoreCase(v.getName()))
                        .findFirst()
                        .ifPresent(oldVaccine -> {
                            if (oldVaccine.getStatus() == VaccinationStatus.PENDING) {
                                // Create next vaccine
                                Vaccination futureC4cv = new Vaccination(
                                        null, C4CV_VACCINE, LocalDate.now().plusDays(15), VaccinationStatus.NEW, pet);
                                vaccinationRepository.save(futureC4cv);
                            }
                        });
            }
        }
    }
}
