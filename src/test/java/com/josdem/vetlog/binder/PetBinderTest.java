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

package com.josdem.vetlog.binder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.command.PetCommand;
import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Breed;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.PetImage;
import com.josdem.vetlog.model.User;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.BreedRepository;
import com.josdem.vetlog.repository.VaccinationRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class PetBinderTest {

    private PetBinder petBinder;

    @Mock
    private BreedRepository breedRepository;

    @Mock
    private VaccinationRepository vaccinationRepository;

    private final List<Vaccination> vaccines = List.of(
            new Vaccination(1L, "DA2PP", LocalDate.now(), VaccinationStatus.APPLIED, null),
            new Vaccination(2L, "Deworming", LocalDate.now(), VaccinationStatus.PENDING, null));

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        petBinder = new PetBinder(breedRepository, vaccinationRepository);
    }

    @Test
    @DisplayName("binding a pet")
    void shouldBindPet(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());

        var breed = getBreed();
        var pet = getPet(breed);

        when(vaccinationRepository.findAllByPet(pet)).thenReturn(vaccines);

        var result = petBinder.bindPet(pet);

        assertEquals(1L, result.getId());
        assertEquals("1b211410-320b-11ed-a261-0242ac120002", result.getUuid());
        assertEquals("Frida", result.getName());
        assertEquals("2021-01-17T00:00", result.getBirthDate());
        assertTrue(result.getDewormed());
        assertTrue(result.getSterilized());
        assertTrue(result.getVaccinated());
        assertFalse(result.getImages().isEmpty());
        assertEquals(1, result.getVaccines().size());
        assertEquals(5L, result.getBreed());
        assertEquals(2L, result.getUser());
        assertEquals(PetType.CAT, result.getType());
    }

    @Test
    @DisplayName("binging a pet from command")
    void shouldBindPetFromCommand(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        var petCommand = new PetCommand();
        petCommand.setId(2L);
        petCommand.setName("Marla");
        petCommand.setBirthDate("2021-01-17T00:00");
        petCommand.setStatus(PetStatus.IN_ADOPTION);
        petCommand.setDewormed(true);
        petCommand.setSterilized(true);
        petCommand.setVaccinated(true);
        petCommand.setImages(Arrays.asList(new PetImage()));
        petCommand.setBreed(1L);
        petCommand.setVaccines(vaccines);

        var breed = getBreed();
        var optionalBreed = Optional.of(breed);
        when(breedRepository.findById(1L)).thenReturn(optionalBreed);
        var result = petBinder.bindPet(petCommand);

        assertEquals(2L, result.getId());
        assertEquals(36, result.getUuid().length());
        assertEquals("Marla", result.getName());
        assertEquals(LocalDateTime.of(2021, 01, 17, 0, 0), result.getBirthDate());
        assertEquals(PetStatus.IN_ADOPTION, result.getStatus());
        assertNotNull(result.getImages());
        assertEquals(breed, result.getBreed());

        verify(vaccinationRepository, times(2)).save(isA(Vaccination.class));
        vaccines.forEach(vaccine -> assertEquals(LocalDate.now(), vaccine.getDate()));
    }

    @Test
    @DisplayName("not binging a pet from command due to breed not found")
    void shouldNotBindPetFromCommandDueToBreedNotFound(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        var petCommand = new PetCommand();
        petCommand.setBirthDate("2021-01-17T00:00");
        assertThrows(BusinessException.class, () -> petBinder.bindPet(petCommand));
    }

    @NotNull
    private Pet getPet(Breed breed) {
        var pet = new Pet();
        pet.setId(1L);
        pet.setUuid("1b211410-320b-11ed-a261-0242ac120002");
        pet.setName("Frida");
        pet.setDewormed(true);
        pet.setSterilized(true);
        pet.setVaccinated(true);
        pet.setImages(List.of(new PetImage()));
        pet.setBreed(breed);
        pet.setBirthDate(LocalDateTime.of(2021, 01, 17, 0, 0, 0));
        pet.setUser(getUser());
        return pet;
    }

    @NotNull
    private User getUser() {
        var user = new User();
        user.setId(2L);
        return user;
    }

    @NotNull
    private Breed getBreed() {
        var breed = new Breed();
        breed.setId(5L);
        breed.setName("Ragdoll");
        breed.setType(PetType.CAT);
        return breed;
    }
}
