package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.TelephoneService
import com.jos.dem.vetlog.repository.PetRepository

@Service
class TelephoneServiceImpl implements TelephoneService {

  @Autowired
  PetService petService
  @Autowired
  PetRepository petRepository

  void save(Command command, User adopter){
    Pet pet = petService.getPetByUuid(command.uuid)
    pet.status = PetStatus.ADOPTED
    pet.adopter = adopter
    petRepository.save(pet)
  }

}
