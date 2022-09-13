package com.jos.dem.vetlog.binder;

import com.jos.dem.vetlog.command.PetLogCommand;
import com.jos.dem.vetlog.model.PetLog;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class PetLogBinderTest {

  private PetLogBinder binder = new PetLogBinder();

  @Test
  @DisplayName("binding a pet log command")
  void shouldBindPetLogCommand(TestInfo testInfo) {
    log.info("Running: {}", testInfo.getDisplayName());
    PetLogCommand petLogCommand = getPetLogCommand();

    PetLog result = binder.bind(petLogCommand);

    assertEquals("Margarita Morales", result.getVetName());
    assertEquals("Digestion issues", result.getSigns());
    assertEquals("Constipation", result.getDiagnosis());
    assertEquals("Stool Ease", result.getMedicine());
  }

  @NotNull
  private PetLogCommand getPetLogCommand() {
    PetLogCommand petLogCommand = new PetLogCommand();
    petLogCommand.setVetName("Margarita Morales");
    petLogCommand.setSigns("Digestion issues");
    petLogCommand.setDiagnosis("Constipation");
    petLogCommand.setMedicine("Stool Ease");
    return petLogCommand;
  }
}
