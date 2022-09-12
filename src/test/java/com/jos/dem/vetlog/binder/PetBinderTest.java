package com.jos.dem.vetlog.binder;

import com.jos.dem.vetlog.command.PetCommand;
import com.jos.dem.vetlog.enums.PetType;
import com.jos.dem.vetlog.model.Breed;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.PetImage;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.BreedRepository;
import com.jos.dem.vetlog.util.DateFormatter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class PetBinderTest {

  private PetBinder petBinder;

  @Mock private BreedRepository breedRepository;
  private DateFormatter dateFormatter = new DateFormatter();

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    petBinder = new PetBinder(breedRepository, dateFormatter);
  }

  @Test
  @DisplayName("binding a pet")
  void shouldBindPet(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    Breed breed = getBreed();
    Pet pet = getPet(breed);

    PetCommand result = petBinder.bindPet(pet);

    assertEquals(1L, result.getId());
    assertEquals("1b211410-320b-11ed-a261-0242ac120002", result.getUuid());
    assertEquals("Frida", result.getName());
    assertEquals("01/17/2021", result.getBirthDate());
    assertTrue(result.getDewormed());
    assertTrue(result.getSterilized());
    assertTrue(result.getVaccinated());
    assertFalse(result.getImages().isEmpty());
    assertEquals(5L, result.getBreed());
    assertEquals(2L, result.getUser());
    assertEquals(PetType.CAT, result.getType());
  }

  @NotNull
  private Pet getPet(Breed breed) {
    Pet pet = new Pet();
    pet.setId(1L);
    pet.setUuid("1b211410-320b-11ed-a261-0242ac120002");
    pet.setName("Frida");
    pet.setDewormed(true);
    pet.setSterilized(true);
    pet.setVaccinated(true);
    pet.setImages(Arrays.asList(new PetImage()));
    pet.setBreed(breed);
    pet.setBirthDate(LocalDateTime.of(2021, 01, 17, 0,0,0));
    pet.setUser(getUser());
    return pet;
  }

  @NotNull
  private User getUser() {
    User user = new User();
    user.setId(2L);
    return user;
  }

  @NotNull
  private Breed getBreed() {
    Breed breed = new Breed();
    breed.setId(5L);
    breed.setName("Ragdoll");
    breed.setType(PetType.CAT);
    return breed;
  }
}
