/*
Copyright 2017 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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

}
