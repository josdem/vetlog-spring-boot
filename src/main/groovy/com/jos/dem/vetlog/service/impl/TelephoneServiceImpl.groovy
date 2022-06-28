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

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User


import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.service.TelephoneService
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.UserRepository

@Service
class TelephoneServiceImpl implements TelephoneService {

  @Autowired
  PetService petService
  @Autowired
  RestService restService
  @Autowired
  UserRepository userRepository
  @Autowired
  PetRepository petRepository

  @Value('${template.adoption.name}')
  String adoptionTemplate

  @Transactional
  void save(Command command, User adopter){
    Pet pet = petService.getPetByUuid(command.uuid)
    pet.status = PetStatus.ADOPTED
    adopter.mobile = command.mobile
    pet.adopter = adopter
    petRepository.save(pet)
    userRepository.save(adopter)
    createAdoptionDataMessage(command, pet)
  }

  private createAdoptionDataMessage(Command command, Pet pet){
    User owner = pet.user
    User adopter = pet.adopter
    Command messageCommand = new MessageCommand(
      email:owner.email,
      name:pet.name,
      contactName: "${adopter.firstname} ${adopter.lastname}",
      emailContact: adopter.email,
      message:adopter.mobile,
      template: adoptionTemplate
    )
    restService.sendCommand(messageCommand)
  }

}
