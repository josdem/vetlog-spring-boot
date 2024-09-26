/*
Copyright 2024 Jose Morales contact@josdem.io

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.josdem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.model.Breed;
import com.josdem.vetlog.repository.BreedRepository;
import com.josdem.vetlog.service.impl.BreedServiceImpl;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class BreedServiceTest {

    private BreedService service;

    @Mock
    private BreedRepository breedRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new BreedServiceImpl(breedRepository);
    }

    @Test
    @DisplayName("getting breeds by type")
    void shouldGetBreedsByType(TestInfo testInfo) {
        log.info("Running: {}", testInfo.getDisplayName());
        when(breedRepository.findByType(PetType.DOG)).thenReturn(Arrays.asList(new Breed()));
        var breeds = service.getBreedsByType(PetType.DOG);
        assertFalse(breeds.isEmpty(), "should have a breed");
    }
}
