package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.service.RegistrationService
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.repository.RegistrationCodeRepository
import com.jos.dem.vetlog.exception.VetlogException

@Service
class RegistrationServiceImpl implements RegistrationService {

  @Autowired
  LocaleService localeService
  @Autowired
  RegistrationCodeRepository repository

  String findEmailByToken(String token){
    RegistrationCode registrationCode = repository.findByToken(token)
    if(!registrationCode) throw new VetlogException(localeService.getMessage('exception.token.not.found'))
    registrationCode.email
  }

  String generateToken(String email){
    RegistrationCode registrationCode = new RegistrationCode(email:email)
    repository.save(registrationCode)
    registrationCode.token
  }

  Boolean validateToken(String token){
    true
  }

}
