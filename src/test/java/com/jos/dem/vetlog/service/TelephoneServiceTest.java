package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.command.TelephoneCommand;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.PetRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.impl.TelephoneServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class TelephoneServiceTest {

  private TelephoneService service;

  @Mock private PetService petService;
  @Mock private RestService restService;
  @Mock private UserRepository userRepository;
  @Mock private PetRepository petRepository;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    service = new TelephoneServiceImpl(petService, restService, userRepository, petRepository);
  }

  @Test
  @DisplayName("sending adopter contact information to the pet owner")
  void shouldSendAdopterInformation(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    TelephoneCommand telephoneCommand = new TelephoneCommand();
    telephoneCommand.setUuid("uuid");
    telephoneCommand.setMobile("7346041832");

    User owner = new User();
    owner.setEmail("contact@josdem.io");

    User adopter = new User();
    adopter.setEmail("athena@gmail.com");

    Pet pet = new Pet();
    pet.setName("Cinnamon");
    pet.setUser(owner);
    pet.setAdopter(adopter);

    when(petService.getPetByUuid("uuid")).thenReturn(pet);
    service.save(telephoneCommand, adopter);

    verify(petRepository).save(pet);
  }
}
