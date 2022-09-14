package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.binder.PetLogBinder;
import com.jos.dem.vetlog.command.PetLogCommand;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.PetLog;
import com.jos.dem.vetlog.repository.PetLogRepository;
import com.jos.dem.vetlog.repository.PetRepository;
import com.jos.dem.vetlog.service.impl.PetLogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@Slf4j
class PetLogServiceTest {

    private PetLogService service;

    @Mock private PetLogBinder petLogBinder;
    @Mock private PetLogRepository petLogRepository;
    @Mock private PetRepository petRepository;

    @BeforeEach
    void setup(TestInfo testInfo){
        MockitoAnnotations.openMocks(this);
        service = new PetLogServiceImpl(petLogBinder, petLogRepository, petRepository);
    }

    @Test
    @DisplayName("saving a pet log")
    void shouldSavePetLog(TestInfo testInfo){
        log.info("Running: {}", testInfo.getDisplayName());
        Pet pet = new Pet();
        PetLogCommand petLogCommand = new PetLogCommand();
        petLogCommand.setPet(1L);
        PetLog petLog = new PetLog();
        petLog.setPet(pet);

        when(petLogBinder.bind(petLogCommand)).thenReturn(petLog);
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        service.save(petLogCommand);
        verify(petLogRepository).save(petLog);
    }
}
