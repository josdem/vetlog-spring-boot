package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.binder.PetBinder;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.PetRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.impl.PetServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class PetServiceTest {

  private PetService service;

  @Mock private PetBinder petBinder;
  @Mock private PetRepository petRepository;
  @Mock private PetImageService petImageService;
  @Mock private UserRepository userRepository;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    service = new PetServiceImpl(petBinder, petRepository, petImageService, userRepository);
  }

  @Test
  @DisplayName("saving a pet")
  void shouldSavePet(TestInfo testInfo) throws IOException {
    log.info("Running: {}", testInfo.getDisplayName());
    User user = new User();
    Pet pet = new Pet();
    Command command = Mockito.mock(Command.class);
    when(petBinder.bindPet(command)).thenReturn(pet);
    service.save(command, user);
    verify(petRepository).save(Mockito.isA(Pet.class));
  }
}
