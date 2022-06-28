package com.jos.dem.vetlog.binder;

import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetLogCommand;
import com.jos.dem.vetlog.model.PetLog;

public class PetLogBinder {

    public PetLog bind(Command command) {
        PetLogCommand petLogCommand = (PetLogCommand) command;
        PetLog petLog = new PetLog();
        petLog.setVetName(petLogCommand.getVetName());
        petLog.setSymptoms(petLogCommand.getSymptoms());
        petLog.setDiagnosis(petLogCommand.getDiagnosis());
        petLog.setMedicine(petLogCommand.getMedicine());
        return petLog;
    }
}
