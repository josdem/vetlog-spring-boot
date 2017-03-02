package com.jos.dem.vetlog.unit

import org.springframework.validation.Errors

import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.ChangePasswordCommand
import com.jos.dem.vetlog.validator.ChangePasswordValidator
import com.jos.dem.vetlog.service.LocaleService

import spock.lang.Specification

class ChangePasswordValidatorSpec extends Specification {

  ChangePasswordValidator validator = new ChangePasswordValidator()

  Errors errors = Mock(Errors)
  LocaleService localeService = Mock(LocaleService)

  def setup(){
    validator.localeService = localeService
  }

  void "should not validate since passwords are not equals"(){
    given:"A user command"
      Command command = new ChangePasswordCommand(token:'token',password:'password', passwordConfirmation:'p4ssword')
    when:"We validate passwords"
      localeService.getMessage('user.validation.password.equals') >> 'The passwords are not equals'
      validator.validate(command, errors)
    then:"We expect valiation failed"
    1 * errors.reject('password', 'The passwords are not equals')
  }

}
