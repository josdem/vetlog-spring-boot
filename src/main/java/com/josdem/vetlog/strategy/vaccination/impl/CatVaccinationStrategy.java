package com.josdem.vetlog.strategy.vaccination.impl;

import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.VaccinationRepository;
import com.josdem.vetlog.strategy.vaccination.VaccinationStrategy;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatVaccinationStrategy implements VaccinationStrategy {
    private static final String TRICAT = "TRICAT";
    private static final String DEWORMING = "Deworming";
    private static final String TRICAT_BOOST = "TRICAT_BOOST";
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
                registerVaccinations(vaccinationRepository, pet, TRICAT, DEWORMING);
            }
            default -> {
                log.info("Annual vaccination");
                registerVaccinations(vaccinationRepository, pet, TRICAT, DEWORMING);
            }
        }
    }

    @Override
    public void updateVaccines(List<Vaccination> previousVaccines, List<Vaccination> newVaccines, Pet pet) {
        for (Vaccination newVaccine : newVaccines) {
            String appliedName = newVaccine.getName();
            boolean wasPending = previousVaccines.stream()
                    .anyMatch(previousVaccine -> appliedName.equalsIgnoreCase(previousVaccine.getName())
                            && previousVaccine.getStatus() == VaccinationStatus.PENDING);

            if (RABIES.equalsIgnoreCase(appliedName) && Boolean.TRUE.equals(pet.getGoingOutOften())) {
                vaccinationRepository.save(
                        new Vaccination(null, FELV, LocalDate.now().plusDays(21), VaccinationStatus.NEW, pet));
            }

            if (newVaccine.getStatus() != VaccinationStatus.APPLIED || !wasPending) {
                continue;
            }

            if (TRICAT.equalsIgnoreCase(appliedName)) {
                long weeks = ChronoUnit.WEEKS.between(pet.getBirthDate(), LocalDate.now());
                long days = ChronoUnit.DAYS.between(pet.getBirthDate(), LocalDate.now());

                if (weeks >= 9 && weeks <= 16) {
                    vaccinationRepository.save(new Vaccination(
                            null, TRICAT_BOOST, LocalDate.now().plusDays(21), VaccinationStatus.NEW, pet));
                }
                if (days > 16 * 7) {
                    vaccinationRepository.save(
                            new Vaccination(null, RABIES, LocalDate.now().plusDays(21), VaccinationStatus.NEW, pet));
                }
            }

            if (TRICAT_BOOST.equalsIgnoreCase(appliedName) || FELV.equalsIgnoreCase(appliedName)) {
                vaccinationRepository.save(
                        new Vaccination(null, RABIES, LocalDate.now().plusDays(21), VaccinationStatus.NEW, pet));
            }

            if (RABIES.equalsIgnoreCase(appliedName)) {
                vaccinationRepository.save(
                        new Vaccination(null, TRICAT, LocalDate.now().plusYears(1), VaccinationStatus.NEW, pet));
            }
        }
    }
}
