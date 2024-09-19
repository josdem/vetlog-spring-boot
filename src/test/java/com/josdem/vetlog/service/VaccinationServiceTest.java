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

package com.josdem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Breed;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.VaccinationRepository;
import com.josdem.vetlog.service.impl.VaccinationServiceImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class VaccinationServiceTest {

    private VaccinationService vaccinationService;

    @Mock
    private VaccinationRepository vaccinationRepository;

    private final Pet pet = new Pet();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        vaccinationService = new VaccinationServiceImpl(vaccinationRepository);
        pet.setBreed(new Breed());
    }

    @Test
    @DisplayName("not saving a pet if it is not a dog")
    void shouldNotSavePetIfItIsNotADog(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());
        pet.getBreed().setType(PetType.CAT);
        assertThrows(BusinessException.class, () -> vaccinationService.save(pet));
    }

    @Test
    @DisplayName("saving first vaccination")
    void shouldSaveFirstVaccination(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());
        pet.getBreed().setType(PetType.DOG);
        pet.setBirthDate(LocalDateTime.now().minusWeeks(6));
        vaccinationService.save(pet);
        verify(vaccinationRepository, times(2))
                .save(new Vaccination(null, any(), LocalDate.now(), VaccinationStatus.PENDING, pet));
    }

    @Test
    @DisplayName("saving second vaccination")
    void shouldSaveSecondVaccination(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());
        pet.getBreed().setType(PetType.DOG);
        pet.setBirthDate(LocalDateTime.now().minusWeeks(10));
        vaccinationService.save(pet);
        verify(vaccinationRepository, times(3))
                .save(new Vaccination(null, any(), LocalDate.now(), VaccinationStatus.PENDING, pet));
    }

    @Test
    @DisplayName("saving third vaccination")
    void shouldSaveThirdVaccination(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());
        pet.getBreed().setType(PetType.DOG);
        pet.setBirthDate(LocalDateTime.now().minusWeeks(14));
        vaccinationService.save(pet);
        verify(vaccinationRepository, times(4))
                .save(new Vaccination(null, any(), LocalDate.now(), VaccinationStatus.PENDING, pet));
    }

    @Test
    @DisplayName("saving annual vaccination")
    void shouldSaveAnnualVaccination(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());
        pet.getBreed().setType(PetType.DOG);
        pet.setBirthDate(LocalDateTime.now().minusWeeks(20));
        vaccinationService.save(pet);
        verify(vaccinationRepository, times(5))
                .save(new Vaccination(null, any(), LocalDate.now(), VaccinationStatus.PENDING ,pet));
    }

    @Test
    @DisplayName("not saving vaccination due is not old enough")
    void shouldNotSaveVaccinationDueToNotOldEnough(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());
        pet.getBreed().setType(PetType.DOG);
        pet.setBirthDate(LocalDateTime.now().minusWeeks(1));
        vaccinationService.save(pet);
        verify(vaccinationRepository, never())
                .save(new Vaccination(null, any(), LocalDate.now(), VaccinationStatus.PENDING, pet));
    }
}
