package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.PetBinder
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.repository.PetRepository

@Service
class PetLogServiceImpl implements PetLogService {

  @Autowired
  PetLogBinder petLogBinder
  @Autowired
  PetLogRepository petLogRepository

  PetLog save(Command command, Pet pet){
    PetLog petLog = petLogBinder.bind(command)
    petLog.pet = pet
    petLogRepository.save(petLog)
    petLog
  }

}
