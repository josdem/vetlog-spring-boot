package com.jos.dem.vetlog.model

import javax.persistence.Entity
import javax.persistence.Column

import com.jos.dem.vetlog.enums.RegistrationCodeStatus

@Entity
class RegistrationCode {

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
