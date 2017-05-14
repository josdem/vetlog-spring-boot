package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetLog
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.PetLogBinder
import com.jos.dem.vetlog.service.PetLogService
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.PetLogRepository

@Service
class PetLogServiceImpl implements PetLogService {

  @Autowired
  PetLogBinder petLogBinder
  @Autowired
  PetLogRepository petLogRepository
  @Autowired
  PetRepository petRepository

  PetLog save(Command command){
    PetLog petLog = petLogBinder.bind(command)
    Pet pet = petRepository.findOne(command.pet)
    petLog.pet = pet
    petLogRepository.save(petLog)
    petLog
  }

  List<PetLog> getPetLogsByPet(Pet pet){
    petLogRepository.getAllByPet(pet)
  }

}
