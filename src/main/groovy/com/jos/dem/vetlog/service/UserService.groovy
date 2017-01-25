package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.User
import  com.jos.dem.vetlog.command.Command

interface UserService {
  User getUserByUsername(String username)
  User getUserByEmail(String email)
  void save(Command command)
}
