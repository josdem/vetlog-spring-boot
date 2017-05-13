package com.jos.dem.vetlog.unit

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.TelephoneCommand
import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.TelephoneService
import com.jos.dem.vetlog.service.impl.TelephoneServiceImpl
import com.jos.dem.vetlog.repository.PetRepository

import spock.lang.Specification

class TelephoneServiceSpec extends Specification{

  TelephoneService service = new TelephoneServiceImpl()

  PetService petService = Mock(PetService)
  PetRepository petRepository = Mock(PetRepository)

  def setup(){
    service.petService = petService
    service.petRepository = petRepository
  }

  void "should save"(){
    given:"A command"
      Command command = new TelephoneCommand(uuid:'uuid')
    and:"A user"
      User user = new User()
    and:"A pet"
      Pet pet = new Pet(name:'Astra Caliope')
    when:"We save adoption"
      petService.getPetByUuid('uuid') >> pet
      service.save(command, user)
    then:"I expect adoption"
    1 * petRepository.save(pet)
    pet.status == PetStatus.ADOPTED
    pet.adopter == user
  }
}
