package com.jos.dem.vetlog

import org.springframework.validation.Errors

import spock.lang.Specification

import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.UserCommand
import com.jos.dem.vetlog.validator.UserValidator

class UserValidatorSpec extends Specification {

  UserValidator validator = new UserValidator()
  Errors errors = Mock(Errors)

  void "should not validate an user command since passwords are not equals"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'p4ssword', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validatePasswords(errors, command)
    then:"We expect valiation failed"
    1 * errors.reject('password', 'The passwords are not equals')
  }

  void "should vaidate an user command"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'password', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validatePasswords(errors, command)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', 'The passwords are not equals')
  }

  void "should accept only characters and numbers in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa4ssword', passwordConfirmation:'p4ssword', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validatePasswords(errors, command)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', 'Passwords are bad formed')
  }

  void "should accept dash character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa-4ssword', passwordConfirmation:'pa-4ssword', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validatePasswords(errors, command)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', 'Passwords are bad formed')
  }

  void "should accept special dot character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa.4ssword', passwordConfirmation:'pa.4ssword', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validatePasswords(errors, command)
    then:"We expect everything is going to be all right"
    0 * errors.reject('password', 'Passwords are bad formed')
  }

}
