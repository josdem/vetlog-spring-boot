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

package com.josdem.vetlog.binder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.josdem.vetlog.command.PetLogCommand;
import com.josdem.vetlog.model.PetLog;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

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
