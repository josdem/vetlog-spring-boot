package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.Pet
import  com.jos.dem.vetlog.command.Command

interface UserService {
  Pet save(Command command)
}
