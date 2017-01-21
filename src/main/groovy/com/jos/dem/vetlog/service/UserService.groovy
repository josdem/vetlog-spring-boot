package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.User

interface UserService {
  User getUserByUsername(String username)
}
