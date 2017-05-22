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

package com.jos.dem.vetlog.model

import static javax.persistence.GenerationType.AUTO
import static javax.persistence.EnumType.STRING

import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.OneToOne
import javax.persistence.OneToMany
import javax.persistence.JoinColumn
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.CascadeType

import com.jos.dem.vetlog.enums.PetStatus

@Entity
class Pet {

  @Id
  @GeneratedValue(strategy=AUTO)
  Long id

  @Column(nullable = false)
  String uuid

  @Column(nullable = false)
  String name

  @Column(nullable = false)
  Date birthDate

  @Column(nullable = false)
  Boolean dewormed = false

  @Column(nullable = false)
  Boolean sterilized = false

  @Column(nullable = false)
  Boolean vaccinated = false

  @Column(nullable = false)
  Date dateCreated = new Date()

  @Column(nullable = false)
  @Enumerated(STRING)
  PetStatus status

  @OneToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="breed_id")
  Breed breed

  @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
  @JoinColumn(name="pet_adoption_id")
  PetAdoption adoption

  @OneToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="user_id")
  User user

  @OneToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="adopter_id")
  User adopter

  @OneToMany(fetch=FetchType.LAZY)
  @JoinColumn(name="pet_image_id")
  List<PetImage> images

}
