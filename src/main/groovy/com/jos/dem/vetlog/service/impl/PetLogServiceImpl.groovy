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
import com.jos.dem.vetlog.model.PetLog
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.PetLogBinder
import com.jos.dem.vetlog.service.PetLogService
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.PetLogRepository

@Service
class PetLogServiceImpl implements PetLogService {

  @Autowired
  PetLogBinder petLogBinder
  @Autowired
  PetLogRepository petLogRepository
  @Autowired
  PetRepository petRepository

  PetLog save(Command command){
    PetLog petLog = petLogBinder.bind(command)
    Pet pet = petRepository.findOne(command.pet)
    petLog.pet = pet
    petLogRepository.save(petLog)
    petLog
  }

  List<PetLog> getPetLogsByPet(Pet pet){
    petLogRepository.getAllByPet(pet)
  }

}
