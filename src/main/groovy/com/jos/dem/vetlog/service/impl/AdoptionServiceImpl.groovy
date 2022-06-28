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
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetAdoption

import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.AdoptionService
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.AdoptionRepository

@Service
class AdoptionServiceImpl implements AdoptionService {

  @Autowired
  AdoptionRepository adoptionRepository
  @Autowired
  PetService petService
  @Autowired
  PetRepository petRepository

  PetAdoption save(Command command){
    Pet pet = petService.getPetByUuid(command.uuid)
    PetAdoption petAdoption = new PetAdoption(
      pet:pet,
      description:command.description
    )
    pet.status = PetStatus.IN_ADOPTION
    pet.adoption = petAdoption
    petRepository.save(pet)
    petAdoption
  }

}
