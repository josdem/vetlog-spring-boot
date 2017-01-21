package com.jos.dem.vetlog.service.impl

import com.jos.dem.vetlog.User
import com.jos.dem.vetlog.CurrentUser
import com.jos.dem.vetlog.UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

import  com.jos.dem.vetlog.model.User
import  com.jos.dem.vetlog.model.CurrentUser
import  com.jos.dem.vetlog.service.UserService

@Service
class CurrentUserDetailService implements UserDetailsService {

  @Autowired
  UserService userService

  @Override
  CurrentUser loadUserByUsername(String username) {
    User user = userService.getUserByUsername(username).orElseThrow{ ->
      throw new RuntimeException(String.format("User with username : ${username} was not found"))
    }
    new CurrentUser(user)
  }

}

