package com.jos.dem.vetlog.service.impl

import com.jos.dem.vetlog.repository.RegistrationCodeRepository

class RegistrationServiceImpl implements RegistrarionService {

  @Autowired
  RegistrationCodeRepository repository

  String findEmailByToken(String token){
    RegistrationCode registrationCode = repository.findByToken(token)
    registrationCode.email
  }

}
