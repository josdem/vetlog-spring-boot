package com.josdem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
                .save(new Vaccination(null, any(), LocalDate.now(), VaccinationStatus.PENDING));
    }
}
