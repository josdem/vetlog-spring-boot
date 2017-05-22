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
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.service.RegistrationService
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.MessageCommand
import com.jos.dem.vetlog.repository.RegistrationCodeRepository
import com.jos.dem.vetlog.repository.UserRepository
import com.jos.dem.vetlog.exception.UserNotFoundException
import com.jos.dem.vetlog.exception.VetlogException

@Service
class RecoveryServiceImpl implements RecoveryService {

  @Autowired
  RestService restService
  @Autowired
  RegistrationService registrationService
  @Autowired
  UserRepository userRepository
  @Autowired
  RegistrationCodeRepository repository
  @Autowired
  LocaleService localeService

  @Value('${server}')
  String server
  @Value('${template.register.name}')
  String registerTemplate
  @Value('${template.register.path}')
  String registerPath
  @Value('${template.forgot.name}')
  String forgotTemplate
  @Value('${template.forgot.path}')
  String forgotPath


  void sendConfirmationAccountToken(String email){
    String token = registrationService.generateToken(email)
    Command command = new MessageCommand(email:email, template:registerTemplate, url:"${server}${registerPath}${token}")
    restService.sendCommand(command)
  }

  User confirmAccountForToken(String token){
    User user = getUserByToken(token)
    if(!user) throw new UserNotFoundException(localeService.getMessage('exception.user.not.found'))
      user.enabled = true
      userRepository.save(user)
      user
  }

  private User getUserByToken(String token){
    String email = registrationService.findEmailByToken(token)
    if(!email) throw new VetlogException(localeService.getMessage('exception.token.not.found'))
      User user = userRepository.findByEmail(email)
      user
  }

  void generateRegistrationCodeForEmail(String email){
    User user = userRepository.findByEmail(email)
    if(!user) throw new UserNotFoundException(localeService.getMessage('exception.user.not.found'))
    if(!user.enabled) throw new VetlogException(localeService.getMessage('exception.account.not.activated'))
    String token = registrationService.generateToken(email)
    Command command = new MessageCommand(email:email, template:forgotTemplate, url:"${server}${forgotPath}${token}")
    restService.sendCommand(command)
  }

  Boolean validateToken(String token){
    RegistrationCode registrationCode = repository.findByToken(token)
    registrationCode == null ? false : true
  }

  User changePassword(Command command){
    User user = getUserByToken(command.token)
    user.password = new BCryptPasswordEncoder().encode(command.password)
    userRepository.save(user)
    user
  }

}
