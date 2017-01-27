package com.jos.dem.vetlog.unit

import spock.lang.Specification

import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.impl.UserServiceImpl
import com.jos.dem.vetlog.repository.UserRepository
import com.jos.dem.vetlog.binder.UserBinder
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.UserCommand

class UserServiceSpec extends Specification {

  UserService userService = new UserServiceImpl()

  UserRepository userRepository = Mock(UserRepository)
  UserBinder userBinder = new UserBinder()

  def setup(){
    userService.userRepository = userRepository
    userService.userBinder = userBinder
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
      Command command = new UserCommand(username:'josdem',password:'password', passwordConfirmation:'password', name:'josdem',lastname:'lastname',email:'josdem@email.com')
    when:"We save an user"
      userService.save(command)
    then:"We expect repository delegation"
    1 * userRepository.save(_ as User)
  }

}
