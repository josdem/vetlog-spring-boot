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
import com.jos.dem.vetlog.model.PetAdoption

import com.jos.dem.vetlog.command.AdoptionCommand
import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.AdoptionService
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.AdoptionRepository
import com.jos.dem.vetlog.service.impl.AdoptionServiceImpl

import spock.lang.Specification

class AdoptionServiceSpec extends Specification {

  AdoptionService service = new AdoptionServiceImpl()

  PetService petService = Mock(PetService)
  PetRepository petRepository = Mock(PetRepository)
  AdoptionRepository adoptionRepository = Mock(AdoptionRepository)

  def setup(){
    service.petService = petService
    service.petRepository = petRepository
    service.adoptionRepository = adoptionRepository
  }

  void "should save"(){
    given:"A adoptionCommand"
      Command adoptionCommand = new AdoptionCommand(uuid:'uuid', description:'description')
    and:"A pet"
      Pet pet = new Pet(status:'OWNED')
    when:"We save"
      petService.getPetByUuid('uuid') >> pet
      PetAdoption result = service.save(adoptionCommand)
    then:"We expect to save adoption"
    1 * petRepository.save(pet)
    pet.status == PetStatus.IN_ADOPTION
    pet.adoption instanceof PetAdoption
    result.pet == pet
    result.description == 'description'
  }

}
