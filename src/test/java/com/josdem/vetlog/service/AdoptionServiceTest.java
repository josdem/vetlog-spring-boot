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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.command.AdoptionCommand;
import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.PetAdoption;
import com.josdem.vetlog.repository.PetRepository;
import com.josdem.vetlog.service.impl.AdoptionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
public class AdoptionServiceTest {

    private AdoptionService service;

    @Mock
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new AdoptionServiceImpl(petService, petRepository);
    }

    @Test
    @DisplayName("saving a pet in adoption")
    void shouldSaveAPetInAdoption(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        AdoptionCommand adoptionCommand = getAdoptionCommand();
        Pet pet = getPet();

        when(petService.getPetByUuid("uuid")).thenReturn(pet);
        PetAdoption result = service.save(adoptionCommand);

        verify(petRepository).save(pet);
        assertEquals(PetStatus.IN_ADOPTION, pet.getStatus());
        assertEquals(pet, result.getPet());
        assertEquals("description", result.getDescription());
    }

    private Pet getPet() {
        Pet pet = new Pet();
        pet.setStatus(PetStatus.OWNED);
        return pet;
    }

    private AdoptionCommand getAdoptionCommand() {
        AdoptionCommand adoptionCommand = new AdoptionCommand();
        adoptionCommand.setUuid("uuid");
        adoptionCommand.setDescription("description");
        return adoptionCommand;
    }
}
