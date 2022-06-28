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

package com.jos.dem.vetlog.binder

import com.jos.dem.vetlog.util.DateFormatter
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.enums.PetStatus

import com.jos.dem.vetlog.util.UuidGenerator

import com.jos.dem.vetlog.repository.UserRepository
import com.jos.dem.vetlog.repository.BreedRepository

@Component
class PetBinder {

  @Autowired
  BreedRepository breedRepository
  @Autowired
  UserRepository userRepository
  @Autowired
  DateFormatter dateFormatter


  Pet bindPet(Command command){
    Pet pet = new Pet(
      id:command.id,
      uuid:UuidGenerator.generateUuid(),
      name:command.name,
      birthDate:Date.parse('MM/dd/yyyy', command.birthDate),
      dewormed:command.dewormed,
      sterilized:command.sterilized,
      vaccinated:command.vaccinated,
      images:command.images,
      status:PetStatus.OWNED,
      breed:breedRepository.findOne(command.breed),
      user:getUser(command.user),
      adopter:getUser(command.adopter)
    )
    pet
  }

  PetCommand bindPet(Pet pet){
    Command command = new PetCommand(
      id:pet.id,
      uuid:pet.uuid,
      name:pet.name,
      birthDate:dateFormatter.format(pet.birthDate.toString()),
      dewormed:pet.dewormed,
      sterilized:pet.sterilized,
      vaccinated:pet.vaccinated,
      status:pet.status,
      images:pet.images,
      breed:pet.breed.id,
      user:pet.user.id,
      type:pet.breed.type
    )
    command
  }

  private User getUser(Long id){
    id ? userRepository.findOne(id) : null
  }

}
