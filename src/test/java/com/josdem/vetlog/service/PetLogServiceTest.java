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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.binder.PetLogBinder;
import com.josdem.vetlog.command.PetLogCommand;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.PetLog;
import com.josdem.vetlog.repository.PetLogRepository;
import com.josdem.vetlog.repository.PetRepository;
import com.josdem.vetlog.service.impl.PetLogServiceImpl;
import java.io.IOException;
import java.util.Arrays;
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
class PetLogServiceTest {

    private PetLogService service;

    private Pet pet = new Pet();

    @Mock
    private PetLogBinder petLogBinder;

    @Mock
    private PetLogRepository petLogRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private PetPrescriptionService petPrescriptionService;

    @BeforeEach
    void setup(TestInfo testInfo) {
        MockitoAnnotations.openMocks(this);
        service = new PetLogServiceImpl(petLogBinder, petLogRepository, petRepository, petPrescriptionService);
    }

    @Test
    @DisplayName("saving a pet log")
    void shouldSavePetLog(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());
        var petLogCommand = new PetLogCommand();
        petLogCommand.setPet(1L);
        var petLog = getPetLog();
        var optionalPet = Optional.of(pet);

        when(petLogBinder.bind(petLogCommand)).thenReturn(petLog);
        when(petRepository.findById(1L)).thenReturn(optionalPet);

        service.save(petLogCommand);
        verify(petLogRepository).save(petLog);
    }

    @Test
    @DisplayName("should not find a pet log")
    void shouldNotFindPetLog(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());
        var petLogCommand = new PetLogCommand();
        var petLog = getPetLog();

        when(petLogBinder.bind(petLogCommand)).thenReturn(petLog);

        assertThrows(BusinessException.class, () -> service.save(petLogCommand));
    }

    @Test
    @DisplayName("getting logs by pet")
    void shouldGetPetLogsByPet(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        var petLog = getPetLog();
        when(petLogRepository.getAllByPet(pet)).thenReturn(Arrays.asList(petLog));
        var result = service.getPetLogsByPet(pet);
        assertEquals(Arrays.asList(petLog), result);
    }

    @NotNull
    private PetLog getPetLog() {
        var petLog = new PetLog();
        petLog.setPet(pet);
        return petLog;
    }
}
