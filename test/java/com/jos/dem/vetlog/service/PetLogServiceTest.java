package com.jos.dem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jos.dem.vetlog.binder.PetLogBinder;
import com.jos.dem.vetlog.command.PetLogCommand;
import com.jos.dem.vetlog.exception.BusinessException;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.PetLog;
import com.jos.dem.vetlog.repository.PetLogRepository;
import com.jos.dem.vetlog.repository.PetRepository;
import com.jos.dem.vetlog.service.impl.PetLogServiceImpl;
import java.io.IOException;
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
        PetLogCommand petLogCommand = new PetLogCommand();
        petLogCommand.setPet(1L);
        PetLog petLog = getPetLog();
        Optional<Pet> optionalPet = Optional.of(pet);

        when(petLogBinder.bind(petLogCommand)).thenReturn(petLog);
        when(petRepository.findById(1L)).thenReturn(optionalPet);

        service.save(petLogCommand);
        verify(petLogRepository).save(petLog);
    }

    @Test
    @DisplayName("should not find a pet log")
    void shouldNotFindPetLog(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());
        PetLogCommand petLogCommand = new PetLogCommand();
        PetLog petLog = getPetLog();

        when(petLogBinder.bind(petLogCommand)).thenReturn(petLog);

        assertThrows(BusinessException.class, () -> service.save(petLogCommand));
    }

    @Test
    @DisplayName("getting logs by pet")
    void shouldGetPetLogsByPet(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        PetLog petLog = getPetLog();
        when(petLogRepository.getAllByPet(pet)).thenReturn(Arrays.asList(petLog));
        List<PetLog> result = service.getPetLogsByPet(pet);
        assertEquals(Arrays.asList(petLog), result);
    }

    @NotNull
    private PetLog getPetLog() {
        PetLog petLog = new PetLog();
        petLog.setPet(pet);
        return petLog;
    }
}
