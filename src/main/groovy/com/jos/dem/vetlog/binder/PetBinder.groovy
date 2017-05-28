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

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.PetCommand
import com.jos.dem.vetlog.util.UuidGenerator
import com.jos.dem.vetlog.repository.BreedRepository

@Component
class PetBinder {

  @Autowired
  BreedRepository breedRepository

  Pet bindPet(Command command){
    Pet pet = new Pet()
    pet.id = command.id
    pet.uuid = UuidGenerator.generateUuid()
    pet.name = command.name
    pet.birthDate = command.birthDate
    pet.dewormed = command.dewormed
    pet.sterilized = command.sterilized
    pet.vaccinated = command.vaccinated
    pet.images = command.images
    pet.status = PetStatus.OWNED
    pet.breed = breedRepository.findOne(command.breed)
    pet
  }

  PetCommand bindPet(Pet pet){
    Command command = new PetCommand(
      id:pet.id,
      uuid:pet.uuid,
      name:pet.name,
      birthDate:pet.birthDate,
      dewormed:pet.dewormed,
      sterilized:pet.sterilized,
      vaccinated:pet.vaccinated,
      status:pet.status,
      breed:pet.breed.id,
      user:pet.user.id
    )
    command
  }

}
