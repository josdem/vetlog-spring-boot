/*
Copyright 2022 Jose Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.binder;

import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetLogCommand;
import com.jos.dem.vetlog.model.PetLog;
import org.springframework.stereotype.Component;

@Component
public class PetLogBinder {

    public PetLog bind(Command command) {
        PetLogCommand petLogCommand = (PetLogCommand) command;
        PetLog petLog = new PetLog();
        petLog.setVetName(petLogCommand.getVetName());
        petLog.setSymptoms(petLogCommand.getSigns());
        petLog.setDiagnosis(petLogCommand.getDiagnosis());
        petLog.setMedicine(petLogCommand.getMedicine());
        return petLog;
    }
}
