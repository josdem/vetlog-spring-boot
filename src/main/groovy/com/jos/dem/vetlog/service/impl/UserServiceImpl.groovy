package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import  com.jos.dem.vetlog.model.User
import  com.jos.dem.vetlog.service.UserService
import  com.jos.dem.vetlog.repository.UserRepository

@Service
class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository

  User getUserByUsername(String username){
    userRepository.findByUsername(username)
  }

}
