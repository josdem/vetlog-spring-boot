package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command

interface TelephoneService {
  void save(Command command, User adopter)
}
