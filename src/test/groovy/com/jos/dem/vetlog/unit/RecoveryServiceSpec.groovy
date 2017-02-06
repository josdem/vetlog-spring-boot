package com.jos.dem.vetlog.unit

import spock.lang.Specification

import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.service.impl.RecoveryServiceImpl
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.service.RegistrationService
import com.jos.dem.vetlog.repository.UserRepository
import com.jos.dem.vetlog.repository.RegistrationCodeRepository
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.command.Command

class RecoveryServiceSpec extends Specification {

  RecoveryService recoveryService = new RecoveryServiceImpl()

  RestService restService = Mock(RestService)
  RegistrationService registrationService = Mock(RegistrationService)
  RegistrationCodeRepository repository = Mock(RegistrationCodeRepository)
  UserRepository userRepository = Mock(UserRepository)

  def setup(){
    recoveryService.restService = restService
    recoveryService.repository = repository
    recoveryService.userRepository = userRepository
    recoveryService.registrationService = registrationService
  }

  void "should send confirmation account token"(){
  given:"An email"
  String email = 'josdem@email.com'
  when:"We call to  send confirmation token"
  recoveryService.sendConfirmationAccountToken(email)
  then:"We expect to persiste and send token"
  1 * repository.save(_ as RegistrationCode)
  1 * restService.sendCommand(_ as Command)
  }

  void "should confirm account for token"(){
  given:"A token"
    String token = 'token'
  and:"An email"
    String email = 'josdem@email.com'
  and:"A user"
    User user = new User()
  when:"We confirm account for token"
    registrationService.findEmailByToken(token) >> email
    userRepository.findByEmail(email) >> user
    recoveryService.confirmAccountForToken(token)
  then:"We expect user enabled"
    user.enabled
    1 * userRepository.save(user)
  }

}
