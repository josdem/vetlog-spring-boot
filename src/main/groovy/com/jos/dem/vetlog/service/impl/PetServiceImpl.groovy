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

package com.jos.dem.vetlog.service.impl

import com.jos.dem.vetlog.command.Command
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.enums.PetStatus

import com.jos.dem.vetlog.binder.PetBinder
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.PetImageService
import com.jos.dem.vetlog.repository.PetRepository

@Service
class PetServiceImpl implements PetService {

  @Autowired
  PetBinder petBinder
  @Autowired
  PetRepository petRepository
  @Autowired
  PetImageService petImageService

  @Transactional
  Pet save(Command command, User user){
    Pet pet = petBinder.bindPet(command)
    pet.user = user
    //TODO: Add save pet image service attach image: https://github.com/josdem/vetlog-spring-boot/issues/123
    petRepository.save(pet)
    pet
  }

  @Transactional
  Pet update(Command command){
    recoveryImages(command)
    Pet pet = petBinder.bindPet(command)
    petImageService.attachImage(command)
    petRepository.save(pet)
    pet
  }

  Pet getPetByUuid(String uuid){
    petRepository.findByUuid(uuid)
  }

  List<Pet> getPetsByUser(User user){
    petRepository.findAllByUser(user) - petRepository.findAllByStatus(PetStatus.ADOPTED) + petRepository.findAllByAdopter(user)
  }

  List<Pet> getPetsByStatus(PetStatus status){
    petRepository.findAllByStatus(status)
  }

  private void recoveryImages(Command command){
    Pet pet = petRepository.findOne(command.id)
    command.images = pet.images
  }

}
