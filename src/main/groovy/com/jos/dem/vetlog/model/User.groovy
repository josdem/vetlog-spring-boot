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
  @Column(nullable = false)
  @Enumerated(STRING)
  Role role

}

