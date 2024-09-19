package com.josdem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Breed;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.service.impl.VaccinationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@Slf4j
class VaccinationServiceTest {

    private final VaccinationService vaccinationService = new VaccinationServiceImpl();

    @Test
    @DisplayName("Should not save a pet if it is not a dog")
    void shouldNotSavePetIfItIsNotADog(TestInfo testInfo) {
        log.info("Test: {}", testInfo.getDisplayName());
        var pet = new Pet();
        pet.setBreed(new Breed());
        pet.getBreed().setType(PetType.CAT);
        assertThrows(BusinessException.class, () -> vaccinationService.save(pet));
    }
}
