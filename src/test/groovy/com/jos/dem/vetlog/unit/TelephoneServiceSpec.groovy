package com.jos.dem.vetlog.unit

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.TelephoneCommand
import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.service.TelephoneService
import com.jos.dem.vetlog.service.impl.TelephoneServiceImpl
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.UserRepository

import spock.lang.Specification

class TelephoneServiceSpec extends Specification{

  TelephoneService service = new TelephoneServiceImpl()

  PetService petService = Mock(PetService)
  PetRepository petRepository = Mock(PetRepository)
  UserRepository userRepository = Mock(UserRepository)
  RestService restService = Mock(RestService)

  def setup(){
    service.petService = petService
    service.petRepository = petRepository
    service.userRepository = userRepository
    service.restService = restService
  }

  void "should save"(){
    given:"A command"
      Command command = new TelephoneCommand(uuid:'uuid', mobile:'5516827055')
    and:"A owner and adopter"
      User owner = new User(email:'josdem@email.com')
      User adopter = new User(email:'estrella@email.com', mobile:'5500012345')
    and:"A pet"
      Pet pet = new Pet(name:'Astra Caliope', user:owner, adopter:adopter)
    when:"We save adoption"
      petService.getPetByUuid('uuid') >> pet
      service.save(command, adopter)
    then:"I expect adoption"
    1 * petRepository.save(pet)
    1 * userRepository.save(adopter)
    adopter.mobile == command.mobile
    pet.status == PetStatus.ADOPTED
    pet.adopter == adopter
    restService.sendCommand(_ as Command)
  }
}
