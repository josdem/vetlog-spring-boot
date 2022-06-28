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

import com.jos.dem.vetlog.binder.PetLogBinder
import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetLog
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.PetLogCommand

import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.PetLogRepository
import com.jos.dem.vetlog.service.PetLogService
import com.jos.dem.vetlog.service.impl.PetLogServiceImpl
import spock.lang.Specification

class PetLogServiceSpec extends Specification {

  PetLogService service = new PetLogServiceImpl()

  PetLogBinder petLogBinder = Mock(PetLogBinder)
  PetLogRepository petLogRepository = Mock(PetLogRepository)
  PetRepository petRepository = Mock(PetRepository)

  def setup(){
    service.petLogBinder = petLogBinder
    service.petLogRepository = petLogRepository
    service.petRepository = petRepository
  }

  void "should save pet"(){
    given:"A command"
      Command command = new PetLogCommand()
    and:"A pet id"
      Long petId = 1L
      command.pet = petId
    and:"A pet and a pet log"
      Pet pet = new Pet()
      PetLog petLog = new PetLog()
    when:"We save pet"
      petLogBinder.bind(command) >> petLog
      petRepository.findById(petId) >> pet
      PetLog result = service.save(command)
    then:"We expect pet saved with user"
      result.pet == pet
      1 * petLogRepository.save(_ as PetLog)
  }

}
