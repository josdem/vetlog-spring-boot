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

import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.model.Breed;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.VaccinationRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private static final String TRICAT_VACCINE = "TRICAT";
    private static final String TRICAT_BOOST_VACCINE = "TRICAT_BOOST";

    private static final Map<String, String> NEXT_VACCINE = Map.of(
            PUPPY_VACCINE, C4CV_VACCINE,
            C4CV_VACCINE, C6CV_VACCINE,
            TRICAT_VACCINE, TRICAT_BOOST_VACCINE);

    private static final Map<String, java.time.Period> NEXT_VACCINE_OFFSET = Map.of(
            PUPPY_VACCINE, java.time.Period.ofDays(15),
            C4CV_VACCINE, java.time.Period.ofDays(15),
            TRICAT_VACCINE, java.time.Period.ofDays(45));

    private static final Map<String, java.time.Period> NEXT_RABIES_VACCINE_OFFSET = Map.of(
            TRICAT_VACCINE, java.time.Period.ofDays(45),
            C6CV_VACCINE, java.time.Period.ofDays(15),
            TRICAT_BOOST_VACCINE, java.time.Period.ofDays(45),
            RABIES_VACCINE, java.time.Period.ofYears(1));

    private final VaccinationRepository vaccinationRepository;

    public void validateRabiesVaccine(List<Vaccination> previousVaccines, List<Vaccination> newVaccines, Pet pet) {
        for (Vaccination newVaccine : newVaccines) {
            String appliedName = newVaccine.getName();
            if (NEXT_RABIES_VACCINE_OFFSET.containsKey(appliedName)
                    && newVaccine.getStatus() == VaccinationStatus.APPLIED
                    && previousVaccines.stream()
                            .anyMatch(previousVaccine -> appliedName.equalsIgnoreCase(previousVaccine.getName())
                                    && previousVaccine.getStatus() == VaccinationStatus.PENDING)) {
                if (TRICAT_VACCINE.equalsIgnoreCase(appliedName) && !isPetOlderThan16Weeks(pet)) continue;
                saveNewVaccine(RABIES_VACCINE, LocalDate.now().plus(NEXT_RABIES_VACCINE_OFFSET.get(appliedName)), pet);
            }
        }
    }

    public void validateNextVaccines(List<Vaccination> previousVaccines, List<Vaccination> newVaccines, Pet pet) {
        for (Vaccination newVaccine : newVaccines) {
            String appliedName = newVaccine.getName();
            if (NEXT_VACCINE.containsKey(appliedName)
                    && newVaccine.getStatus() == VaccinationStatus.APPLIED
                    && previousVaccines.stream()
                            .anyMatch(previousVaccine -> appliedName.equalsIgnoreCase(previousVaccine.getName())
                                    && previousVaccine.getStatus() == VaccinationStatus.PENDING)) {
                String nextName = NEXT_VACCINE.get(appliedName);
                if (!isSpecificCriteriaSatisfiedForApplyingNextVaccine(appliedName, nextName, pet)) continue;
                saveNewVaccine(nextName, LocalDate.now().plus(NEXT_VACCINE_OFFSET.get(appliedName)), pet);
            }
        }
    }

    private boolean isSpecificCriteriaSatisfiedForApplyingNextVaccine(String appliedName, String nextName, Pet pet) {
        if (TRICAT_VACCINE.equalsIgnoreCase(appliedName) && TRICAT_BOOST_VACCINE.equalsIgnoreCase(nextName)) {
            return Optional.ofNullable(pet)
                    .map(Pet::getBreed)
                    .map(Breed::getType)
                    .filter(PetType.CAT::equals)
                    .flatMap(type -> Optional.ofNullable(pet.getBirthDate()))
                    .map(birthDate -> ChronoUnit.WEEKS.between(birthDate, LocalDate.now()))
                    .map(weeks -> weeks >= 9 && weeks <= 16)
                    .orElse(false);
        }
        return true;
    }

    private boolean isPetOlderThan16Weeks(Pet pet) {
        return Optional.ofNullable(pet)
                .map(Pet::getBirthDate)
                .map(dob -> LocalDate.now().isAfter(dob.plusWeeks(16)))
                .orElse(false);
    }

    private void saveNewVaccine(String name, LocalDate date, Pet pet) {
        Vaccination future = new Vaccination(null, name, date, VaccinationStatus.NEW, pet);
        vaccinationRepository.save(future);
    }
}
