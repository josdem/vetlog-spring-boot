package com.jos.dem.vetlog.unit

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.PetBinder
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.impl.PetServiceImpl

import spock.lang.Specification

class PetServiceSpec extends Specification {

  PetService service = new PetServiceImpl()

  PetBinder petBinder = Mock(PetBinder)
  PetRepository petRepository = Mock(PetRepository)

  def setup(){
    service.petBinder = petBinder
    service.petRepository = petRepository
  }

  void "should save pet"(){
    given:"And User"
      User user = new User()
    and:"A command"
      Command command = Mock(Command)
    and:"A pet"
      Pet pet = new Pet()
    when:"We save pet"
      petBinder.bindPet(command) >> pet
      Pet result = service.save(command, user)
    then:"We expect pet saved with user"
      pet.user
      1 * petRepository.save(_ as Pet)
  }

}
