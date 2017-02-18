package com.jos.dem.vetlog.model

import static javax.persistence.EnumType.STRING
import static javax.persistence.GenerationType.AUTO

import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue

@Entity
class Breed {

  @Id
  @GeneratedValue(strategy=AUTO)
  Long id
  @Column(nullable = false)
  String name
  @Column(nullable = false)
  @Enumerated(STRING)
  PetType type
  @Column(nullable = false)
  Date dateCreated = new Date()

}
