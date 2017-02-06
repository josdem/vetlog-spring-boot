package com.jos.dem.vetlog.unit

import spock.lang.Specification

import com.jos.dem.vetlog.service.RegistrationService
import com.jos.dem.vetlog.service.impl.RegistrationServiceImpl
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.repository.RegistrationCodeRepository
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.exception.VetlogException

class RegistrationServiceSpec extends Specification {

  RegistrationService registrationService = new RegistrationServiceImpl()

  RegistrationCodeRepository repository = Mock(RegistrationCodeRepository)
  LocaleService localeService = Mock(LocaleService)

  def setup(){
    registrationService.repository = repository
    registrationService.localeService = localeService
  }

  void "should find email by token"(){
    given:"An token"
      String token = 'token'
    and:"A registration code"
      RegistrationCode registrationCode = new RegistrationCode(email:'josdem@email.com')
    when:"We find registration code by token"
     repository.findByToken(token) >> registrationCode
     String result = registrationService.findEmailByToken(token)
    then:"We expect email"
     'josdem@email.com' == result
  }

  void "should not find email by token since token does not exist"(){
    given:"An token"
      String token = 'token'
    when:"We find registration code by token"
     localeService.getMessage('exception.token.not.found') >> 'Token not found'
     String result = registrationService.findEmailByToken(token)
    then:"We expect exception"
     thrown VetlogException
  }

}
