package com.jos.dem.vetlog.binder

import org.springframework.stereotype.Component

import com.jos.dem.vetlog.model.PetLog
import com.jos.dem.vetlog.command.Command

@Component
class PetBinder {

  PetLog bind(Command command){
    PetLog petLog = new PetLog()
    petLog.vetName = command.vetName
    petLog.symptoms = command.symptoms
    petLog.diagnosis = command.diagnosis
    petLog.medicine = command.medicine
    petLog
  }

}
