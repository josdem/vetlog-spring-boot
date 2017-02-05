package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.service.RegistrationService
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.repository.RegistrationCodeRepository

@Service
class RegistrationServiceImpl implements RegistrationService {

  @Autowired
  RegistrationCodeRepository repository

  String findEmailByToken(String token){
    RegistrationCode registrationCode = repository.findByToken(token)
    registrationCode.email
  }

}
