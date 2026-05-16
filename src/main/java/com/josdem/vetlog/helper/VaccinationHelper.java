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

    private static final Map<String, String> NEXT_VACCINE = Map.of(
            PUPPY_VACCINE, C4CV_VACCINE,
            C4CV_VACCINE, C6CV_VACCINE);

    private static final Map<String, java.time.Period> NEXT_VACCINE_OFFSET = Map.of(
            PUPPY_VACCINE, java.time.Period.ofDays(15),
            C4CV_VACCINE, java.time.Period.ofDays(15));

    private static final Map<String, java.time.Period> NEXT_RABIES_VACCINE_OFFSET = Map.of(
            C6CV_VACCINE, java.time.Period.ofDays(15),
            RABIES_VACCINE, java.time.Period.ofYears(1));

    private final VaccinationRepository vaccinationRepository;

    public void validateRabiesVaccine(List<Vaccination> previousVaccines, List<Vaccination> newVaccines, Pet pet) {
        for (Vaccination newVaccine : newVaccines) {
            String appliedName = newVaccine.getName();
            if (NEXT_RABIES_VACCINE_OFFSET.containsKey(appliedName)
                    && newVaccine.getStatus() == VaccinationStatus.APPLIED) {
                previousVaccines.stream()
                        .filter(v -> appliedName.equalsIgnoreCase(v.getName()))
                        .findFirst()
                        .ifPresent(oldVaccine -> {
                            if (oldVaccine.getStatus() == VaccinationStatus.PENDING) {
                                java.time.Period offset = NEXT_RABIES_VACCINE_OFFSET.getOrDefault(
                                        appliedName, java.time.Period.ofYears(1));
                                Vaccination futureRabies = new Vaccination(
                                        null, RABIES_VACCINE, LocalDate.now().plus(offset), VaccinationStatus.NEW, pet);
                                vaccinationRepository.save(futureRabies);
                            }
                        });
            }
        }
    }

    public void validateNextVaccines(List<Vaccination> previousVaccines, List<Vaccination> newVaccines, Pet pet) {
        for (Vaccination newVaccine : newVaccines) {
            String appliedName = newVaccine.getName();
            if (NEXT_VACCINE.containsKey(appliedName) && newVaccine.getStatus() == VaccinationStatus.APPLIED) {
                previousVaccines.stream()
                        .filter(v -> appliedName.equalsIgnoreCase(v.getName()))
                        .findFirst()
                        .ifPresent(oldVaccine -> {
                            if (oldVaccine.getStatus() == VaccinationStatus.PENDING) {
                                String nextName = NEXT_VACCINE.get(appliedName);
                                java.time.Period offset =
                                        NEXT_VACCINE_OFFSET.getOrDefault(appliedName, java.time.Period.ofDays(15));
                                Vaccination future = new Vaccination(
                                        null, nextName, LocalDate.now().plus(offset), VaccinationStatus.NEW, pet);
                                vaccinationRepository.save(future);
                            }
                        });
            }
        }
    }
}
