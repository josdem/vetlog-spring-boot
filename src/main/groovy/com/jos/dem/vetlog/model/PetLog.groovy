package com.jos.dem.vetlog.model

import static javax.persistence.GenerationType.AUTO

import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.ManyToOne
import javax.persistence.JoinColumn
import javax.persistence.FetchType

@Entity
class PetLog {

  @Id
  @GeneratedValue(strategy=AUTO)
  Long id

  @Column(nullable = true)
  String vetName

  @Column(nullable = false)
  String symptoms

  @Column(nullable = false)
  String diagnosis

  @Column(nullable = true)
  String medicine

  @Column(nullable = false)
  Date dateCreated = new Date()

  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="pet_id")
  Pet pet

}
