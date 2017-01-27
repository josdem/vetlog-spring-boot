package com.jos.dem.vetlog.unit

import spock.lang.Specification

import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.impl.UserServiceImpl
import com.jos.dem.vetlog.repository.UserRepository

class UserServiceSpec extends Specification {

  UserService userService = new UserServiceImpl()

  UserRepository userRepository = Mock(UserRepository)

  def setup(){
    userService.userRepository = userRepository
  }

  void "should find an user by username"(){
    given:"An username"
      String username = 'username'
    when:"We find by username"
      userService.getByUsername(username)
    then:"We expect repository delegation"
    1 * userRepository.findByUsername(username)
  }
}
