package com.josdem.vetlog.strategy.vaccination.impl;

import com.josdem.vetlog.model.Pet;
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
public class CatVaccinationStrategy implements VaccinationStrategy {
    private static final String TRICAT = "TRICAT";
    private static final String TRICAT_BOOST = "TRICAT_BOOST";
    private static final String DEWORMING = "Deworming";
    private static final String RABIES = "Rabies";
    private static final String FELV = "FeLV";

    private final VaccinationRepository vaccinationRepository;

    @Override
    public void vaccinate(Pet pet) {
        long weeks = ChronoUnit.WEEKS.between(pet.getBirthDate(), LocalDate.now());

        switch ((int) weeks) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8 -> {
                log.info("First vaccination");
                registerVaccinations(vaccinationRepository, pet, DEWORMING);
            }
            case 9, 10, 11, 12, 13, 14, 15, 16 -> {
                log.info("Second vaccination");
                registerVaccinations(vaccinationRepository, pet, TRICAT, TRICAT_BOOST, DEWORMING, RABIES, FELV);
            }
            default -> {
                log.info("Annual vaccination");
                registerVaccinations(vaccinationRepository, pet, TRICAT, DEWORMING, RABIES, FELV);
            }
        }
    }
}
