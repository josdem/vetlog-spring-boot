package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.User
import  com.jos.dem.vetlog.command.Command

interface UserService {
  User getByUsername(String username)
  User getByEmail(String email)
  void save(Command command)
}
