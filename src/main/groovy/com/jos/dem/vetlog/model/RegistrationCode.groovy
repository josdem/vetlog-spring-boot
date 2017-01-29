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
