package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.enums.PetType;
import com.jos.dem.vetlog.model.Breed;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.repository.BreedRepository;
import com.jos.dem.vetlog.service.impl.BreedServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@Slf4j
class BreedServiceTest {

    private BreedService service;
    @Mock private BreedRepository breedRepository;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
        service = new BreedServiceImpl(breedRepository);
    }

    @Test
    @DisplayName("getting breeds by type")
    void shouldGetBreedsByType(TestInfo testInfo){
        log.info("Running: {}", testInfo.getDisplayName());
        when(breedRepository.findByType(PetType.DOG)).thenReturn(Arrays.asList(new Breed()));
        List<Breed> breeds = service.getBreedsByType(PetType.DOG);
        assertFalse(breeds.isEmpty(), "should have a breed");
    }
}
