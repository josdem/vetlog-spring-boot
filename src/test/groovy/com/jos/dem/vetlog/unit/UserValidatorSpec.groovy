package com.jos.dem.vetlog

import org.springframework.validation.Errors

import spock.lang.Specification

import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.UserCommand
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.validator.UserValidator

class UserValidatorSpec extends Specification {

  UserValidator validator = new UserValidator()
  Errors errors = Mock(Errors)
  LocaleService localeService = Mock(LocaleService)

  def setup(){
    validator.localeService = localeService
  }

  void "should not validate an user command since passwords are not equals"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'p4ssword', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage('user.validation.password.equals') >> 'The passwords are not equals'
      validator.validate(command, errors)
    then:"We expect valiation failed"
    1 * errors.reject('password', 'The passwords are not equals')
  }

  void "should vaidate an user command"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'password', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', _ as String)
  }

  void "should accept only characters and numbers in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa4ssword', passwordConfirmation:'p4ssword', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage('user.validation.password.equals') >> 'Passwords are bad formed'
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', null)
  }

  void "should accept dash character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa-4ssword', passwordConfirmation:'pa-4ssword', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage('user.validation.password.equals') >> 'Passwords are bad formed'
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', 'Passwords are bad formed')
  }

  void "should accept special dot character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa.4ssword', passwordConfirmation:'pa.4ssword', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage('user.validation.password.equals') >> 'Passwords are bad formed'
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', 'Passwords are bad formed')
  }

  void "should not validate an user command since passwords are too short"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pass', passwordConfirmation:'pass', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validate(command, errors)
    then:"We expect valiation failed"
    1 * errors.reject('password', null)
  }


}
