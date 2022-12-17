package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.binder.PetBinder;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetCommand;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class PetServiceTest {

  private PetService service;

  @Mock private PetBinder petBinder;
  @Mock private PetRepository petRepository;
  @Mock private PetImageService petImageService;
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
  @DisplayName("updating a pet")
  void shouldUpdatePet(TestInfo testInfo) throws IOException {
    log.info("Running: {}", testInfo.getDisplayName());
    pet.setImages(new ArrayList<>());
    PetCommand command = mock(PetCommand.class);
    when(command.getId()).thenReturn(2L);
    when(petRepository.findById(2L)).thenReturn(Optional.of(pet));
    when(petBinder.bindPet(command)).thenReturn(pet);
    when(command.getUser()).thenReturn(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    service.update(command);
    assertEquals(user, pet.getUser());
    verify(petImageService).attachFile(command);
    verify(petRepository).save(Mockito.isA(Pet.class));
    verify(command).setImages(Mockito.isA(List.class));
  }

  @Test
  @DisplayName("not update due to user not found")
  void shouldNotUpdateDueToUserDoesNotExist(TestInfo testInfo){
    log.info("Running: {}", testInfo.getDisplayName());
    PetCommand command = mock(PetCommand.class);
    when(petRepository.findById(2L)).thenReturn(Optional.of(pet));
    assertThrows(BusinessException.class, () -> service.update(command));
  }

  @Test
  @DisplayName("not update due to pet not found")
  void shouldNotUpdateDueToPetDoesNotExist(TestInfo testInfo){
    log.info("Running: {}", testInfo.getDisplayName());
    PetCommand command = mock(PetCommand.class);
    assertThrows(BusinessException.class, () -> service.update(command));
  }

  @Test
  @DisplayName("getting pet by uuid")
  void shouldGetPetByUuid(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    when(petRepository.findByUuid("uuid")).thenReturn(pet);
    assertEquals(pet, service.getPetByUuid("uuid"));
  }

  @Test
  @DisplayName("getting pet by id")
  void shouldGetPetById(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    Optional<Pet> optionalPet = Optional.of(pet);
    when(petRepository.findById(1L)).thenReturn(optionalPet);
    assertEquals(pet, service.getPetById(1L));
  }

  @Test
  @DisplayName("pet by id not found")
  void shouldNotFoundPetById(TestInfo testInfo) {
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
  void shouldGetPetByStatus(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    when(petRepository.findAllByStatus(PetStatus.OWNED)).thenReturn(pets);
    assertEquals(pets, service.getPetsByStatus(PetStatus.OWNED));
  }
}
