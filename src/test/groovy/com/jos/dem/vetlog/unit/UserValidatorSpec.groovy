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

package com.jos.dem.vetlog.unit

import com.jos.dem.vetlog.command.Command
import org.springframework.validation.Errors

import spock.lang.Specification

import  com.jos.dem.vetlog.model.User

import com.jos.dem.vetlog.command.UserCommand
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.validator.UserValidator

class UserValidatorSpec extends Specification {

  UserValidator validator = new UserValidator()
  Errors errors = Mock(Errors)
  UserService userService = Mock(UserService)

  def setup(){
    validator.userService = userService
  }

  void "should not validate an user command since passwords are not equals"(){
    given:"A user command"
    Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'p4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validate(command, errors)
    then:"We expect valiation failed"
    1 * errors.rejectValue('password', 'user.error.password.equals')
  }

  void "should vaidate an user command"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'password', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.rejectValue('password', _ as String)
  }

  void "should accept characters and numbers in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'p4ssword', passwordConfirmation:'p4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.rejectValue('password', _ as String)
  }

  void "should accept dash character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa-4ssword', passwordConfirmation:'pa-4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.rejectValue('password', _ as String)
  }

  void "should accept underscore character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa_4ssword', passwordConfirmation:'pa_4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.rejectValue('password', _ as String)
  }

  void "should accept dot character in password"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa.4ssword', passwordConfirmation:'pa.4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We validate passwords"
      validator.validate(command, errors)
    then:"We expect everything is going to be all right"
    0 * errors.rejectValue('password', _ as String)
  }

  void "should not duplicate an username"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa.4ssword', passwordConfirmation:'pa.4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    and:"User"
      User user = new User()
    when:"We validate user and user already exist by username"
      userService.getByUsername('josdem') >> user
      validator.validate(command, errors)
    then:"We expected an error"
    1 * errors.rejectValue('username', 'user.error.duplicated.username')
  }

  void "should not duplicate an email"(){
    given:"A user command"
      Command command = new UserCommand(username:'josdem',password:'pa.4ssword', passwordConfirmation:'pa.4ssword', firstname:'josdem',lastname:'lastname',email:'josdem@email.com')
    and:"User"
      User user = new User()
    when:"We validate user and user already exist by email"
      userService.getByEmail('josdem@email.com') >> user
      validator.validate(command, errors)
    then:"We expected an error"
    1 * errors.rejectValue('email', 'user.error.duplicated.email')
  }

}
