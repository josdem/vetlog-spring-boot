package com.jos.dem.vetlog.unit

import org.springframework.validation.Errors

import spock.lang.Specification

import  com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.UserCommand
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.validator.UserValidator

class UserValidatorSpec extends Specification {

  UserValidator validator = new UserValidator()
  Errors errors = Mock(Errors)
  LocaleService localeService = Mock(LocaleService)
  UserService userService = Mock(UserService)

  def setup(){
    validator.localeService = localeService
    validator.userService = userService
  }

  void "should not validate an user command since passwords are not equals"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'p4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage('user.validation.password.equals') >> 'The passwords are not equals'
      validator.validate(command, errors)
    then:"We expect valiation failed"
    1 * errors.reject('password', 'The passwords are not equals')
  }

  void "should vaidate an user command"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'password', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage(_ as String) >> 'Passwords are bad formed'
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', _ as String)
  }

  void "should accept characters and numbers in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'p4ssword', passwordConfirmation:'p4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage(_ as String) >> 'Passwords are bad formed'
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', _ as String)
  }

  void "should accept dash character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa-4ssword', passwordConfirmation:'pa-4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage(_ as String) >> 'Passwords are bad formed'
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', _ as String)
  }

  void "should accept underscore character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa_4ssword', passwordConfirmation:'pa_4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage(_ as String) >> 'Passwords are bad formed'
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', _ as String)
  }

  void "should accept dot character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa.4ssword', passwordConfirmation:'pa.4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      localeService.getMessage(_ as String) >> 'Passwords are bad formed'
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', _ as String)
  }

  void "should not duplicate an username"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa.4ssword', passwordConfirmation:'pa.4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    and:"User"
      User user = new User()
    when:"We validate user and user already exist by username"
      localeService.getMessage('user.validation.duplicated.username') >> 'This user is already taken'
      userService.getByUsername('josdem') >> user
      validator.validate(command, errors)
    then:"We expected an error"
    1 * errors.reject('username', 'This user is already taken')
  }

  void "should not duplicate an email"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa.4ssword', passwordConfirmation:'pa.4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    and:"User"
      User user = new User()
    when:"We validate user and user already exist by email"
      localeService.getMessage('user.validation.duplicated.email') >> 'This email is already taken'
      userService.getByEmail('josdem@email.com') >> user
      validator.validate(command, errors)
    then:"We expected an error"
    1 * errors.reject('email', 'This email is already taken')
  }

}
