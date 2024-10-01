package com.josdem.vetlog.strategy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.VaccinationRepository;
import com.josdem.vetlog.strategy.vaccination.impl.DogVaccinationStrategy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class DogVaccinationStrategyTest {

    private DogVaccinationStrategy dogVaccinationStrategy;

    @Mock
    private VaccinationRepository vaccinationRepository;

    private final Pet pet = new Pet();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        dogVaccinationStrategy = new DogVaccinationStrategy(vaccinationRepository);
    }

    @DisplayName("saving vaccines")
    @ParameterizedTest
    @CsvSource({"6, 2", "10, 3", "14, 4", "20, 5"})
    void shouldVaccinatePet(int weeks, int times) {
        log.info("Running test: saving vaccines");
        pet.setBirthDate(LocalDateTime.now().minusWeeks(weeks));
        dogVaccinationStrategy.vaccinate(pet);
        verify(vaccinationRepository, times(times))
                .save(new Vaccination(null, any(), LocalDate.now(), VaccinationStatus.PENDING, pet));
    }

    @Test
    @DisplayName("not saving vaccination due is not old enough")
    void shouldNotVaccinatePet(TestInfo testInfo) {
        log.info("Running test: {}", testInfo.getDisplayName());
        pet.setBirthDate(LocalDateTime.now().minusWeeks(1));
        dogVaccinationStrategy.vaccinate(pet);
        verify(vaccinationRepository, never()).save(any(Vaccination.class));
    }
}
