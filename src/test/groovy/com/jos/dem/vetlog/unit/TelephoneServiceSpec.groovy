/*
Copyright 2017 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.jos.dem.vetlog.unit

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User


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
