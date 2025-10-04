package com.josdem.vetlog.strategy.vaccination;

import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import com.josdem.vetlog.repository.VaccinationRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface VaccinationStrategy {
    void vaccinate(Pet pet);

    default void registerVaccinations(VaccinationRepository vaccinationRepository, Pet pet, String... names) {
        List<Vaccination> vaccinations = Arrays.stream(names)
                .map(name -> new Vaccination(null, name, LocalDate.now(), VaccinationStatus.PENDING, pet))
                .toList();

        vaccinationRepository.saveAll(vaccinations);
    }
}
