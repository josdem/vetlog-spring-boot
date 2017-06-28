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

package com.jos.dem.vetlog.util

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Breed
import com.jos.dem.vetlog.enums.PetType
import com.jos.dem.vetlog.repository.BreedRepository

@Component
class BreedResolver {

  @Autowired
  BreedRepository breedRepository

  Long resolve(PetType type, Long breedId){
    List<Breed> breeds = breedRepository.findAll()
    for(Breed it: breeds) {
      if(it.type.value == type.value) {
        return breedId - it.id
      }
    }
    return 0L
  }

}
