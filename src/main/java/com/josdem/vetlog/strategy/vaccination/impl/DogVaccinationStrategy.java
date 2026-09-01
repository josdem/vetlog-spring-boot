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
public class DogVaccinationStrategy implements VaccinationStrategy {

    private static final String C6CV = "C6CV";
    private static final String DEWORMING = "Deworming";
    private static final String PUPPY = "Puppy";
    private static final String C4CV = "C4CV";
    private static final String RABIES = "Rabies";

    private final VaccinationRepository vaccinationRepository;

    @Override
    public void vaccinate(Pet pet) {
        long weeks = ChronoUnit.WEEKS.between(pet.getBirthDate(), LocalDate.now());

        switch ((int) weeks) {
            case 0, 1, 2, 3, 4, 5 -> {
                log.info("First vaccination");
                registerVaccinations(vaccinationRepository, pet, DEWORMING);
            }
            case 6, 7, 8, 9, 10, 11, 12 -> {
                log.info("Second vaccination");
                registerVaccinations(vaccinationRepository, pet, PUPPY, DEWORMING);
            }
            default -> {
                log.info("Annual vaccination");
                registerVaccinations(vaccinationRepository, pet, C6CV, DEWORMING);
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

            if (newVaccine.getStatus() != VaccinationStatus.APPLIED || !wasPending) {
                continue;
            }

            if (PUPPY.equalsIgnoreCase(appliedName)) {
                vaccinationRepository.save(
                        new Vaccination(null, C4CV, LocalDate.now().plusDays(15), VaccinationStatus.NEW, pet));
            }

            if (C4CV.equalsIgnoreCase(appliedName)) {
                vaccinationRepository.save(
                        new Vaccination(null, C6CV, LocalDate.now().plusDays(15), VaccinationStatus.NEW, pet));
            }

            if (C6CV.equalsIgnoreCase(appliedName)) {
                vaccinationRepository.save(
                        new Vaccination(null, C6CV, LocalDate.now().plusYears(1), VaccinationStatus.NEW, pet));
                vaccinationRepository.save(
                        new Vaccination(null, RABIES, LocalDate.now().plusDays(15), VaccinationStatus.NEW, pet));
            }
        }
    }
}
