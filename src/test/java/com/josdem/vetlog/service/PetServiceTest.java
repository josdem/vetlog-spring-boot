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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.binder.PetBinder;
import com.josdem.vetlog.command.Command;
import com.josdem.vetlog.command.PetCommand;
import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.PetAdoption;
import com.josdem.vetlog.model.User;
import com.josdem.vetlog.repository.AdoptionRepository;
import com.josdem.vetlog.repository.PetRepository;
import com.josdem.vetlog.repository.UserRepository;
import com.josdem.vetlog.service.impl.PetServiceImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@Slf4j
class PetServiceTest {

    private PetService service;

    @Mock
    private PetBinder petBinder;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetImageService petImageService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdoptionRepository adoptionRepository;

    @Mock
    private LocaleService localeService;

    private User user;
    private User adopter;
    private Pet pet;
    private List<Pet> pets = new ArrayList<>();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        adopter = new User();
        pet = new Pet();
        service = new PetServiceImpl(
                petBinder, petRepository, petImageService, userRepository, adoptionRepository, localeService);
    }

    @Test
    @DisplayName("saving a pet")
    void shouldSavePet(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());
        Command command = mock(Command.class);
        when(petBinder.bindPet(command)).thenReturn(pet);
        service.save(command, user);
        verify(petRepository).save(Mockito.isA(Pet.class));
    }

    @Test
    @DisplayName("updating a pet")
    void shouldUpdatePet(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());
        pet.setImages(new ArrayList<>());
        PetCommand command = mock(PetCommand.class);
        when(command.getId()).thenReturn(2L);
        when(petRepository.findById(2L)).thenReturn(Optional.of(pet));
        when(petBinder.bindPet(command)).thenReturn(pet);
        when(command.getUser()).thenReturn(1L);
        when(command.getAdopter()).thenReturn(3L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(3L)).thenReturn(Optional.of(adopter));
        service.update(command);
        assertEquals(user, pet.getUser());
        assertEquals(adopter, pet.getAdopter());
        verify(petImageService).attachFile(command);
        verify(petRepository).save(Mockito.isA(Pet.class));
        verify(command).setImages(Mockito.isA(List.class));
    }

    @Test
    @DisplayName("not update due to user not found")
    void shouldNotUpdateDueToUserDoesNotExist(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        PetCommand command = mock(PetCommand.class);
        when(petRepository.findById(2L)).thenReturn(Optional.of(pet));
        assertThrows(BusinessException.class, () -> service.update(command));
    }

    @Test
    @DisplayName("not update due to pet not found")
    void shouldNotUpdateDueToPetDoesNotExist(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        PetCommand command = mock(PetCommand.class);
        assertThrows(BusinessException.class, () -> service.update(command));
    }

    @Test
    @DisplayName("getting pet by uuid")
    void shouldGetPetByUuid(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(petRepository.findByUuid("uuid")).thenReturn(Optional.of(pet));
        assertEquals(pet, service.getPetByUuid("uuid"));
    }

    @Test
    @DisplayName("getting pet by id")
    void shouldGetPetById(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        Optional<Pet> optionalPet = Optional.of(pet);
        when(petRepository.findById(1L)).thenReturn(optionalPet);
        assertEquals(pet, service.getPetById(1L));
    }

    @Test
    @DisplayName("pet by id not found")
    void shouldNotFoundPetById(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        assertThrows(BusinessException.class, () -> service.getPetById(1L));
    }

    @Test
    @DisplayName("listing a pet by owner")
    void shouldListPetsByOwner(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(petRepository.findAllByUser(user)).thenReturn(Arrays.asList(pet));
        when(petRepository.findAllByAdopter(user)).thenReturn(new ArrayList<>());
        when(petRepository.findAllByStatus(PetStatus.ADOPTED)).thenReturn(new ArrayList<>());
        assertEquals(1, service.getPetsByUser(user).size());
    }

    @Test
    @DisplayName("not listing pet if adopted")
    void shouldNotListPetsIfAdopted(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        pets.add(pet);
        when(petRepository.findAllByUser(user)).thenReturn(pets);
        when(petRepository.findAllByAdopter(user)).thenReturn(new ArrayList<>());
        when(petRepository.findAllByStatus(PetStatus.ADOPTED)).thenReturn(pets);
        assertTrue(service.getPetsByUser(user).isEmpty());
    }

    @Test
    @DisplayName("listing pets if I am adopter")
    void shouldListPetIfIamAdopter(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(petRepository.findAllByUser(user)).thenReturn(new ArrayList<>());
        when(petRepository.findAllByAdopter(user)).thenReturn(Arrays.asList(pet));
        when(petRepository.findAllByStatus(PetStatus.ADOPTED)).thenReturn(new ArrayList<>());
        assertEquals(1, service.getPetsByUser(user).size());
    }

    @Test
    @DisplayName("getting pet by status")
    void shouldGetPetByStatus(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(petRepository.findAllByStatus(PetStatus.OWNED)).thenReturn(pets);
        assertEquals(pets, service.getPetsByStatus(PetStatus.OWNED));
    }

    @Test
    @DisplayName("Getting pet adoption information")
    void shouldGetPetAdoption(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        PetAdoption petAdoption = new PetAdoption();
        petAdoption.setId(1L);
        petAdoption.setDescription("It is cute!");
        Optional<PetAdoption> optional = Optional.of(petAdoption);
        pets.add(pet);

        when(adoptionRepository.findByPet(pet)).thenReturn(optional);

        service.getPetsAdoption(pets);
        assertEquals("It is cute!", pets.get(0).getAdoption().getDescription());
    }

    @Test
    @DisplayName("deleting a pet")
    void deletePetByIdSuccessufully(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        assertDoesNotThrow(() -> service.deletePetById(1L));
    }

    @Test
    @DisplayName("not deleting a pet due to not found")
    void shouldNotDeletePetById(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(petRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> service.deletePetById(1L));
    }

    @Test
    @DisplayName("not deleting a pet due to IN_ADOPTION status")
    void shouldNotDeletePetInAdoption(TestInfo testInfo) {
        // given
        log.info("Running: {}", testInfo.getDisplayName());
        var petInAdoption = new Pet();
        petInAdoption.setStatus(PetStatus.IN_ADOPTION);
        when(petRepository.findById(1L)).thenReturn(Optional.of(petInAdoption));

        // when
        assertThrows(BusinessException.class, () -> service.deletePetById(1L));
    }
}
