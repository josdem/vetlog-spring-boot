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

import java.text.SimpleDateFormat

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.model.Breed
import com.jos.dem.vetlog.enums.PetType
import com.jos.dem.vetlog.binder.PetBinder
import com.jos.dem.vetlog.command.PetCommand
import com.jos.dem.vetlog.util.DateFormatter

import spock.lang.Specification

class PetBinderSpec extends Specification {

  PetBinder binder = new PetBinder()

  DateFormatter dateFormatter = Mock(DateFormatter)
  Date birthDate = Mock(Date)

  def setup(){
    binder.dateFormatter = dateFormatter
    dateFormatter.format('2017-06-26 18:32:37') >> '06/26/2017'
    birthDate.toString() >> '2017-06-26 18:32:37'
  }

  void "should bind a pet"(){
    given:'A Breed'
      Breed breed = new Breed(
        name:'Ragdoll',
        type:PetType.CAT
      )
    and:'A Pet'
      Pet pet = new Pet(
        name:'Frida',
        breed:breed,
        birthDate:birthDate,
        user:new User()
      )
    when:'We bind'
      PetCommand result = binder.bindPet(pet)
    then:'We expect pet command'
      result.name == 'Frida'
      result.breed == 45L
  }

}
