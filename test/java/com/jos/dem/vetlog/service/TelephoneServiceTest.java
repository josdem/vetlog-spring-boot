package com.jos.dem.vetlog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jos.dem.vetlog.command.MessageCommand;
import com.jos.dem.vetlog.command.TelephoneCommand;
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.PetRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.impl.TelephoneServiceImpl;
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
        TelephoneCommand telephoneCommand = new TelephoneCommand();
        telephoneCommand.setUuid("uuid");
        telephoneCommand.setMobile("7346041832");

        User owner = getUser("contact@josdem.io");
        User adopter = getUser("athena@gmail.com");
        Pet pet = getPet(owner, adopter);

        when(petService.getPetByUuid("uuid")).thenReturn(pet);
        service.save(telephoneCommand, adopter);

        verify(petRepository).save(pet);
        verify(userRepository).save(adopter);
        verify(restService).sendMessage(Mockito.isA(MessageCommand.class));
        assertEquals(PetStatus.ADOPTED, pet.getStatus());
        assertEquals(adopter, pet.getAdopter());
    }

    private Pet getPet(User owner, User adopter) {
        Pet pet = new Pet();
        pet.setName("Cinnamon");
        pet.setUser(owner);
        pet.setAdopter(adopter);
        return pet;
    }

    private User getUser(String email) {
        User user = new User();
        user.setEmail(email);
        return user;
    }
}
