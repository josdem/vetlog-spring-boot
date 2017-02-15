package com.jos.dem.vetlog.binder

import org.springframework.stereotype.Component
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.model.Role
import com.jos.dem.vetlog.command.Command

@Component
class UserBinder {

  User bindUser(Command command){
    User user = new User()
    user.username = command.username
    user.password = new BCryptPasswordEncoder().encode(command.password)
    user.role = Role.USER
    user.firstName = command.username
    user.lastName = command.lastname
    user.email = command.email
    user
  }

}
