package com.jos.dem.vetlog.unit

import spock.lang.Specification

import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.service.impl.RecoveryServiceImpl
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.service.RegistrationService
import com.jos.dem.vetlog.repository.UserRepository
import com.jos.dem.vetlog.repository.RegistrationCodeRepository
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.exception.UserNotFoundException
import com.jos.dem.vetlog.exception.VetlogException

class RecoveryServiceSpec extends Specification {

  RecoveryService recoveryService = new RecoveryServiceImpl()

  RestService restService = Mock(RestService)
  LocaleService localeService = Mock(LocaleService)
  RegistrationService registrationService = Mock(RegistrationService)
  RegistrationCodeRepository repository = Mock(RegistrationCodeRepository)
  UserRepository userRepository = Mock(UserRepository)

  def setup(){
    recoveryService.restService = restService
    recoveryService.localeService = localeService
    recoveryService.repository = repository
    recoveryService.userRepository = userRepository
    recoveryService.registrationService = registrationService
  }

  void "should send confirmation account token"(){
  given:"An email"
    String email = 'josdem@email.com'
  and:"A token"
    String token = 'token'
  when:"We call to  send confirmation token"
    registrationService.generateToken(email) >> token
    recoveryService.sendConfirmationAccountToken(email)
  then:"We expect to persiste and send token"
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

  void "should not confirm account for token since user not found"(){
  given:"A token"
    String token = 'token'
  and:"An email"
    String email = 'josdem@email.com'
  when:"We confirm account for token"
    localeService.getMessage('exception.user.not.found') >> 'User not found'
    registrationService.findEmailByToken(token) >> email
    recoveryService.confirmAccountForToken(token)
  then:"We expect user enabled"
    thrown UserNotFoundException
  }

  void "should validate token"(){
    given:"A Registration code"
      RegistrationCode registrationCode = new RegistrationCode()
    and:"A token"
      String token = 'token'
    when:"We validate token"
      repository.findByToken(token) >> registrationCode
      Boolean result = recoveryService.validateToken(token)
    then:"We expect true"
      result
  }

  void "should not validate token"(){
    given:"A token"
      String token = 'token'
    when:"We validate token"
      Boolean result = recoveryService.validateToken(token)
    then:"We expect true"
      result == false
  }

  void "should generate registration token for email"(){
    given:"An user"
      User user = new User(enabled:true)
    and:"An email"
      String email = 'josdem@email.com'
    and:"A token"
      String token = 'token'
    when:"We generate registration token for email"
      userRepository.findByEmail(email) >> user
      registrationService.generateToken(email) >> token
      recoveryService.generateRegistrationCodeForEmail(email)
    then:"We expect send command"
    1 * restService.sendCommand(_ as Command)
  }

  void "should not generate registration token for email since user not found"(){
    given:"An email"
      String email = 'josdem@email.com'
    when:"We generate registration token for email"
      localeService.getMessage('exception.user.not.found') >> 'User not found'
      recoveryService.generateRegistrationCodeForEmail(email)
    then:"We expect user not found exception"
    thrown UserNotFoundException
  }

  void "should not generate registration token for email since user not enabled"(){
    given:"An user"
      User user = new User()
    and:"An email"
      String email = 'josdem@email.com'
    when:"We generate registration token for email"
      userRepository.findByEmail(email) >> user
      localeService.getMessage('exception.account.not.activated') >> 'Account not activated'
      recoveryService.generateRegistrationCodeForEmail(email)
    then:"We expect vetlog exception"
    thrown VetlogException
  }


}
