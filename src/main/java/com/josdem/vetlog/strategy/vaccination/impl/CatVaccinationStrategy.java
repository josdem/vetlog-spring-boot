package com.josdem.vetlog.strategy.vaccination.impl;

import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.VaccinationRepository;
import com.josdem.vetlog.strategy.vaccination.VaccinationStrategy;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
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

        if (weeks >= 0 && weeks <= 8) {
            log.info("No vaccination needed");
        } else if (weeks >= 9 && weeks <= 16) {
            log.info("Initial vaccination");
            registerVaccinations(pet, TRICAT, TRICAT_BOOST, DEWORMING, RABIES, FELV);
        } else {
            log.info("Annual vaccination");
            registerVaccinations(pet, TRICAT, DEWORMING, RABIES, FELV);
        }
    }

    private void registerVaccinations(Pet pet, String... names) {
        List<Vaccination> vaccinations = Arrays.stream(names)
                .map(name -> new Vaccination(null, name, LocalDate.now(), VaccinationStatus.PENDING, pet))
                .toList();

        vaccinationRepository.saveAll(vaccinations);
    }
}
