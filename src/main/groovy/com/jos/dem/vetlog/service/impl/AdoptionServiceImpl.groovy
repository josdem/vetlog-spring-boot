package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetAdoption
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.AdoptionService
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.AdoptionRepository

@Service
class AdoptionServiceImpl implements AdoptionService {

  @Autowired
  AdoptionRepository adoptionRepository
  @Autowired
  PetService petService
  @Autowired
  PetRepository petRepository

  PetAdoption save(Command command){
    Pet pet = petService.getPetByUuid(command.uuid)
    PetAdoption petAdoption = new PetAdoption(
      pet:pet,
      description:command.description
    )
    adoptionRepository.save(petAdoption)
    petAdoption
  }

}
