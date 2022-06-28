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

import com.jos.dem.vetlog.binder.PetBinder
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.PetImageService
import com.jos.dem.vetlog.service.impl.PetServiceImpl

import spock.lang.Specification

class PetServiceSpec extends Specification {

  PetService service = new PetServiceImpl()

  PetBinder petBinder = Mock(PetBinder)
  PetRepository petRepository = Mock(PetRepository)
  PetImageService petImageService = Mock(PetImageService)

  def setup(){
    service.petBinder = petBinder
    service.petRepository = petRepository
    service.petImageService = petImageService
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
      1 * petImageService.attachImage(command)
  }

  void "should list a pet by owner"(){
    given:"An owner"
      User user = new User()
    and:"A pet"
      Pet pet = new Pet()
    when:"We list by user"
      petRepository.findAllByUser(user) >> [pet]
      petRepository.findAllByAdopter(user) >> []
      List<Pet> result = service.getPetsByUser(user)
    then:"We expect a pet"
    1 == result.size()
  }

  void "should not list a pet if adopted"(){
    given:"An owner"
      User user = new User()
    and:"A pet"
      Pet pet = new Pet(status:PetStatus.ADOPTED)
    when:"We list by user"
      petRepository.findAllByUser(user) >> [pet]
      petRepository.findAllByStatus(PetStatus.ADOPTED) >> [pet]
      petRepository.findAllByAdopter(user) >> []
      List<Pet> result = service.getPetsByUser(user)
    then:"We expect a pet"
    0 == result.size()
  }

  void "should list a pet if I am the adopter"(){
    given:"An owner"
      User user = new User()
    and:"A pet"
      Pet pet = new Pet(status:PetStatus.ADOPTED)
    when:"We list by user"
      petRepository.findAllByUser(user) >> []
      petRepository.findAllByStatus(PetStatus.ADOPTED) >> []
      petRepository.findAllByAdopter(user) >> [pet]
      List<Pet> result = service.getPetsByUser(user)
    then:"We expect a pet"
    1 == result.size()
  }



}
