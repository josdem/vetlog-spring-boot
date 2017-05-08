package com.jos.dem.vetlog.model

import static javax.persistence.GenerationType.AUTO

import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.JoinColumn
import javax.persistence.FetchType

@Entity
class PetAdoption {

  @Id
  @GeneratedValue(strategy=AUTO)
  Long id

  @Column(nullable = false, columnDefinition="text")
  String desctiption

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="pet_id")
  Pet pet

}

