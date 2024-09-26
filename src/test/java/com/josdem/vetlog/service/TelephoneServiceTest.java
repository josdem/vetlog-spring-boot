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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.command.MessageCommand;
import com.josdem.vetlog.command.TelephoneCommand;
import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.User;
import com.josdem.vetlog.repository.PetRepository;
import com.josdem.vetlog.repository.UserRepository;
import com.josdem.vetlog.service.impl.TelephoneServiceImpl;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@Slf4j
class TelephoneServiceTest {

    private TelephoneService service;

    @Mock
    private PetService petService;

    @Mock
    private RestService restService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new TelephoneServiceImpl(petService, restService, userRepository, petRepository);
    }

    @Test
    @DisplayName("sending adopter contact information to the pet owner")
    void shouldSendAdopterInformation(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());
        var telephoneCommand = new TelephoneCommand();
        telephoneCommand.setUuid("uuid");
        telephoneCommand.setMobile("7346041832");

        var owner = getUser("contact@josdem.io");
        var adopter = getUser("athena@gmail.com");
        var pet = getPet(owner, adopter);

        when(petService.getPetByUuid("uuid")).thenReturn(pet);
        service.save(telephoneCommand, adopter);

        verify(petRepository).save(pet);
        verify(userRepository).save(adopter);
        verify(restService).sendMessage(Mockito.isA(MessageCommand.class));
        assertEquals(PetStatus.ADOPTED, pet.getStatus());
        assertEquals(adopter, pet.getAdopter());
    }

    private Pet getPet(User owner, User adopter) {
        var pet = new Pet();
        pet.setName("Cinnamon");
        pet.setUser(owner);
        pet.setAdopter(adopter);
        return pet;
    }

    private User getUser(String email) {
        var user = new User();
        user.setEmail(email);
        return user;
    }
}
