package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.UserBinder
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.repository.UserRepository

@Service
class UserServiceImpl implements UserService {

  @Autowired
  UserBinder userBinder
  @Autowired
  UserRepository userRepository
  @Autowired
  RecoveryService recoveryService

  User getByUsername(String username){
    userRepository.findByUsername(username)
  }

  User getByEmail(String email){
    userRepository.findByEmail(email)
  }

  User save(Command command){
    User user = userBinder.bindUser(command)
    userRepository.save(user)
    recoveryService.sendConfirmationAccountToken(user.email)
    user
  }

}
