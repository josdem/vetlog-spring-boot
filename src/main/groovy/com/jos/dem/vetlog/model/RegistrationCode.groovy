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

import javax.persistence.Id
import javax.persistence.Entity
import javax.persistence.Column
import javax.persistence.GeneratedValue

import com.jos.dem.vetlog.enums.RegistrationCodeStatus

@Entity
class RegistrationCode {

  @Id
  @GeneratedValue(strategy=AUTO)
  Long id
  @Column(nullable = false)
  String email
  @Column(nullable = false)
  Date dateCreated = new Date()
  @Column(nullable = false)
  String token = UUID.randomUUID().toString().replaceAll('-','')
  @Column(nullable = false)
  RegistrationCodeStatus status = RegistrationCodeStatus.VALID

  Boolean isValid(){
    status == RegistrationCodeStatus.VALID ? true : false
  }
}
