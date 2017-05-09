package com.jos.dem.vetlog.unit

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetAdoption
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.AdoptionCommand
import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.AdoptionService
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.AdoptionRepository
import com.jos.dem.vetlog.service.impl.AdoptionServiceImpl

import spock.lang.Specification

class AdoptionServiceSpec extends Specification {

  AdoptionService service = new AdoptionServiceImpl()

  PetService petService = Mock(PetService)
  PetRepository petRepository = Mock(PetRepository)
  AdoptionRepository adoptionRepository = Mock(AdoptionRepository)

  def setup(){
    service.petService = petService
    service.petRepository = petRepository
    service.adoptionRepository = adoptionRepository
  }

  void "should save"(){
    given:"A adoptionCommand"
      Command adoptionCommand = new AdoptionCommand(uuid:'uuid', description:'description')
    and:"A pet"
      Pet pet = new Pet(status:'OWNED')
    when:"We save"
      petService.getPetByUuid('uuid') >> pet
      PetAdoption result = service.save(adoptionCommand)
    then:"We expect to save adoption"
    1 * petRepository.save(pet)
    1 * adoptionRepository.save(_ as PetAdoption)
    pet.status == PetStatus.IN_ADOPTION
    result.pet == pet
    result.description == 'description'
  }

}
