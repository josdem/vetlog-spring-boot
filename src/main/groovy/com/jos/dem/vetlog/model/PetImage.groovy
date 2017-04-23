package com.jos.dem.vetlog.model

import static javax.persistence.GenerationType.AUTO

import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue

@Entity
class PetImage {

  @Id
  @GeneratedValue(strategy=AUTO)
  Long id

  @Column(nullable = false)
  String uuid

}
