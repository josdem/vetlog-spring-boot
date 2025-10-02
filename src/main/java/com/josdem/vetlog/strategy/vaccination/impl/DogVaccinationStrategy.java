package com.josdem.vetlog.strategy.vaccination.impl;

import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.VaccinationRepository;
import com.josdem.vetlog.strategy.vaccination.VaccinationStrategy;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DogVaccinationStrategy implements VaccinationStrategy {

    private static final String C6CV = "C6CV";
    private static final String DEWORMING = "Deworming";
    private static final String RABIES = "Rabies";
    private static final String PUPPY = "Puppy";
    private static final String C4CV = "C4CV";

    private final VaccinationRepository vaccinationRepository;

    @Override
    public void vaccinate(Pet pet) {
        long weeks = ChronoUnit.WEEKS.between(pet.getBirthDate(), LocalDate.now());

        switch ((int) weeks) {
            case 0, 1, 2, 3, 4, 5 -> log.info("No vaccination needed");
            case 6, 7, 8, 9, 10, 11, 12 -> {
                log.info("First vaccination");
                registerVaccination(PUPPY, pet);
                registerVaccination(DEWORMING, pet);
                registerVaccination(C4CV, pet);
                registerVaccination(C6CV, pet);
                registerVaccination(RABIES, pet);
            }
            default -> {
                log.info("Annual vaccination");
                registerVaccination(C6CV, pet);
                registerVaccination(DEWORMING, pet);
                registerVaccination(RABIES, pet);
            }
        }
    }

    private void registerVaccination(String name, Pet pet) {
        vaccinationRepository.save(new Vaccination(null, name, LocalDate.now(), VaccinationStatus.PENDING, pet));
    }
}
