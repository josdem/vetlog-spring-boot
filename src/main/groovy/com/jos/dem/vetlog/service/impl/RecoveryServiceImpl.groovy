package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.MessageCommand
import com.jos.dem.vetlog.repository.RegistrationCodeRepository

@Service
class RecoveryServiceImpl implements RecoveryService {

  @Autowired
  RestService restService
  @Autowired
  RegistrationCodeRepository repository

  void sendConfirmationAccountToken(String email){
    RegistrationCode registrationCode = new RegistrationCode(email)
    repository.save(registrationCode)
    Command message = new MessageCommand(email:email, template:'register.ftl', url:'http://localhost:8081/emailer/message')
    restService.sendCommand(command)
  }

}
