package com.jos.dem.vetlog.model

import static javax.persistence.GenerationType.AUTO

import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue

@Entity
class Pet {

  @Id
  @GeneratedValue(strategy=AUTO)
  Long id
  @Column(nullable = false)
  String name
  @Column(nullable = false)
  Integer age
  @Column(nullable = false)
  Boolean dewormed = false
  @Column(nullable = false)
  Boolean sterilized = false
  @Column(nullable = false)
  Boolean vaccinated = false
  @Column(nullable = false)
  Breed breed
  @Column(nullable = false)
  Date dateCreated = new Date()

}
