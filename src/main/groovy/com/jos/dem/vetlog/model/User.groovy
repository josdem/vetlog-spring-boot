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

import static javax.persistence.EnumType.STRING
import static javax.persistence.GenerationType.AUTO

import java.io.Serializable
import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue

@Entity
class User implements Serializable {

  @Id
  @GeneratedValue(strategy=AUTO)
  Long id
  @Column(unique = true, nullable = false)
  String username
  @Column(nullable = false)
  String password
  @Column(nullable = true)
  String firstname
  @Column(nullable = true)
  String lastname
  @Column(nullable = true)
  String email
  @Column(nullable = true)
  String mobile
  @Column(nullable = false)
  @Enumerated(STRING)
  Role role

  @Column(nullable = false)
  Boolean enabled = false
  @Column(nullable = false)
  Boolean accountNonExpired = true
  @Column(nullable = false)
  Boolean credentialsNonExpired = true
  @Column(nullable = false)
  Boolean accountNonLocked = true
  @Column(nullable = false)
  Date dateCreated = new Date()

}

