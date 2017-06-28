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

import com.jos.dem.vetlog.model.Breed
import com.jos.dem.vetlog.enums.PetType
import com.jos.dem.vetlog.util.BreedResolver
import com.jos.dem.vetlog.repository.BreedRepository

import spock.lang.Specification

class BreedResolverSpec extends Specification {

  BreedResolver resolver = new BreedResolver()

  BreedRepository breedRepository = Mock(BreedRepository)

  List<Breed> breeds = [
    new Breed(id:1L, name:'Labrador', type:PetType.DOG),
    new Breed(id:2L, name:'Landrace', type:PetType.DOG),
    new Breed(id:3L, name:'German Shepherd', type:PetType.DOG),
    new Breed(id:4L, name:'Siamese', type:PetType.CAT),
    new Breed(id:5L, name:'British Shorthair', type:PetType.CAT),
    new Breed(id:6L, name:'Persian', type:PetType.CAT),
    new Breed(id:7L, name:'Ragdoll', type:PetType.CAT)
  ]

  def setup(){
    resolver.breedRepository = breedRepository
    breedRepository.findAll() >> breeds
  }

  void "should get breed index based in a type"(){
    given:'A type'
      PetType type = PetType.CAT
    and:'A breed id'
      Long breedId = 6L
    when:'We resolve breed index'
      Long result = resolver.resolve(type, breedId)
    then:'We expect sub id'
      result ==  3L
  }

}
