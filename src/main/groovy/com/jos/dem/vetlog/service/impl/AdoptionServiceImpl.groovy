package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetLog
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.PetLogBinder
import com.jos.dem.vetlog.service.PetLogService
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.PetLogRepository

@Service
class PetLogServiceImpl implements PetLogService {

  @Autowired
  AdoptionRepository adoptionRepository
  @Autowired
  PetRepository petRepository

  PetAdoption save(Command command){
    Pet pet = petService.getPetByUuid(command.uuid)
    PetAdoption petAdoption = new PetAdoption()
    petAdoption.pet = pet
    adoptionRepository.save(petAdoption)
    petAdoption
  }

}
