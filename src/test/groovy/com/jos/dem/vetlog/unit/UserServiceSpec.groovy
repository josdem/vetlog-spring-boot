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

import spock.lang.Specification

import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.service.impl.UserServiceImpl
import com.jos.dem.vetlog.repository.UserRepository
import com.jos.dem.vetlog.binder.UserBinder
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.model.Role
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.UserCommand

class UserServiceSpec extends Specification {

  UserService userService = new UserServiceImpl()

  UserRepository userRepository = Mock(UserRepository)
  RecoveryService recoveryService = Mock(RecoveryService)
  UserBinder userBinder = new UserBinder()

  def setup(){
    userService.userRepository = userRepository
    userService.userBinder = userBinder
    userService.recoveryService = recoveryService
  }

  void "should find an user by username"(){
    given:"An username"
      String username = 'username'
    when:"We find by username"
      userService.getByUsername(username)
    then:"We expect repository delegation"
    1 * userRepository.findByUsername(username)
  }

  void "should find an user by email"(){
    given:"An email"
      String email = 'email'
    when:"We find by email"
      userService.getByEmail(email)
    then:"We expect repository delegation"
    1 * userRepository.findByEmail(email)
  }

  void "should save an user"(){
    given:"A command"
      Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'password',firstname:'Jose Luis',lastname:'lastname',email:'josdem@email.com')
    when:"We save an user"
      User user = userService.save(command)
    then:"We expect repository delegation"
    1 * userRepository.save(_ as User)
    1 * recoveryService.sendConfirmationAccountToken('josdem@email.com')
    user.username == 'josdem'
    user.password.size() == 60
    user.firstname == 'Jose Luis'
    user.lastname == 'lastname'
    user.email == 'josdem@email.com'
    user.role == Role.USER
  }

}
