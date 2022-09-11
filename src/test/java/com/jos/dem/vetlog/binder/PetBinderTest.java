package com.jos.dem.vetlog.binder;

import com.jos.dem.vetlog.command.PetCommand;
import com.jos.dem.vetlog.enums.PetType;
import com.jos.dem.vetlog.model.Breed;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class PetBinderTest {

  private PetBinder petBinder = new PetBinder();

  @Test
  @DisplayName("binding a pet")
  void shouldBindPet(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    Breed breed = new Breed();
    breed.setId(5L);
    breed.setName("Ragdoll");
    breed.setType(PetType.CAT);

    Pet pet = new Pet();
    pet.setName("Frida");
    pet.setBreed(breed);
    pet.setBirthDate(LocalDate.now());
    pet.setUser(new User());

    PetCommand result = petBinder.bindPet(pet);
    assertEquals("Frida", result.getName());
    assertEquals(5L, result.getBreed());
    assertEquals(PetType.CAT, result.getType());
  }
}
