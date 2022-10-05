package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.binder.PetBinder;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.exception.BusinessException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@Slf4j
class PetServiceTest {

  private PetService service;

  @Mock private PetBinder petBinder;
  @Mock private PetRepository petRepository;
  @Mock private PetPrescriptionService petImageService;
  @Mock private UserRepository userRepository;

  private User user;
  private Pet pet;
  private List<Pet> pets = new ArrayList<>();

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    pet = new Pet();
    service = new PetServiceImpl(petBinder, petRepository, petImageService, userRepository);
  }

  @Test
  @DisplayName("saving a pet")
  void shouldSavePet(TestInfo testInfo) throws IOException {
    log.info("Running: {}", testInfo.getDisplayName());
    Command command = mock(Command.class);
    when(petBinder.bindPet(command)).thenReturn(pet);
    service.save(command, user);
    verify(petRepository).save(Mockito.isA(Pet.class));
  }

  @Test
  @DisplayName("getting pet by uuid")
  void shouldGetPetByUuid(TestInfo testInfo){
    log.info("Running: {}", testInfo.getDisplayName());
    when(petRepository.findByUuid("uuid")).thenReturn(pet);
    assertEquals(pet, service.getPetByUuid("uuid"));
  }

  @Test
  @DisplayName("getting pet by id")
  void shouldGetPetById(TestInfo testInfo){
    log.info("Running: {}", testInfo.getDisplayName());
    Optional<Pet> optionalPet = Optional.of(pet);
    when(petRepository.findById(1L)).thenReturn(optionalPet);
    assertEquals(pet, service.getPetById(1L));
  }

  @Test
  @DisplayName("pet by id not found")
  void shouldNotFoundPetById(TestInfo testInfo){
    log.info("Running: {}", testInfo.getDisplayName());
    assertThrows(BusinessException.class, () -> service.getPetById(1L));
  }

  @Test
  @DisplayName("listing a pet by owner")
  void shouldListPetsByOwner(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    when(petRepository.findAllByUser(user)).thenReturn(Arrays.asList(pet));
    when(petRepository.findAllByAdopter(user)).thenReturn(new ArrayList<>());
    when(petRepository.findAllByStatus(PetStatus.ADOPTED)).thenReturn(new ArrayList<>());
    assertEquals(1, service.getPetsByUser(user).size());
  }

  @Test
  @DisplayName("not listing pet if adopted")
  void shouldNotListPetsIfAdopted(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    pets.add(pet);
    when(petRepository.findAllByUser(user)).thenReturn(pets);
    when(petRepository.findAllByAdopter(user)).thenReturn(new ArrayList<>());
    when(petRepository.findAllByStatus(PetStatus.ADOPTED)).thenReturn(pets);
    assertTrue(service.getPetsByUser(user).isEmpty());
  }

  @Test
  @DisplayName("listing pets if I am adopter")
  void shouldListPetIfIamAdopter(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    when(petRepository.findAllByUser(user)).thenReturn(new ArrayList<>());
    when(petRepository.findAllByAdopter(user)).thenReturn(Arrays.asList(pet));
    when(petRepository.findAllByStatus(PetStatus.ADOPTED)).thenReturn(new ArrayList<>());
    assertEquals(1, service.getPetsByUser(user).size());
  }

  @Test
  @DisplayName("getting pet by status")
  void shouldGetPetByStatus(TestInfo testInfo){
    log.info("Running: {}", testInfo.getDisplayName());
    when(petRepository.findAllByStatus(PetStatus.OWNED)).thenReturn(pets);
    assertEquals(pets, service.getPetsByStatus(PetStatus.OWNED));
  }
}
