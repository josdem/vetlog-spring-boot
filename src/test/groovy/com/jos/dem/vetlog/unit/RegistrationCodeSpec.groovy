package com.jos.dem.vetlog.unit

import spock.lang.Specification

import com.jos.dem.vetlog.model.RegistrationCode

class RegistrationCodeSpec extends Specification {

  RegistrationCode registrationCode = new RegistrationCode()

  void "should create a registration code with creation date"(){
    given:"A date"
      Date now = new Date()
    expect:"Date is not null and in the past"
      registrationCode.dateCreated.getTime() - now.getTime() < 10000
      registrationCode.token.size() == 32
      registrationCode.isValid()
  }
	
}