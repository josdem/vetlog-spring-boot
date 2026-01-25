/*
  Copyright 2026 Jose Morales contact@josdem.io

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

import com.josdem.vetlog.command.Command;
import com.josdem.vetlog.command.PetLogCommand;
import com.josdem.vetlog.model.PetLog;
import org.springframework.stereotype.Component;

@Component
public class PetLogBinder {

    public PetLog bind(Command command) {
        PetLogCommand petLogCommand = (PetLogCommand) command;
        PetLog petLog = new PetLog();
        petLog.setId(petLogCommand.getId());
        petLog.setVetName(petLogCommand.getVetName());
        petLog.setSigns(petLogCommand.getSigns());
        petLog.setDiagnosis(petLogCommand.getDiagnosis());
        petLog.setMedicine(petLogCommand.getMedicine());
        petLog.setUuid(petLogCommand.getUuid());
        petLog.setHasAttachment(petLogCommand.isHasAttachment());
        return petLog;
    }

    public PetLogCommand bind(PetLog petLog) {
        PetLogCommand petLogCommand = new PetLogCommand();
        petLogCommand.setId(petLog.getId());
        petLogCommand.setVetName(petLog.getVetName());
        petLogCommand.setSigns(petLog.getSigns());
        petLogCommand.setDiagnosis(petLog.getDiagnosis());
        petLogCommand.setMedicine(petLog.getMedicine());
        petLogCommand.setUuid(petLog.getUuid());
        petLogCommand.setHasAttachment(petLog.isHasAttachment());
        return petLogCommand;
    }
}
