package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.Pet
import  com.jos.dem.vetlog.model.User
import  com.jos.dem.vetlog.command.Command

interface PetService {
  Pet save(Command command, User user)
  List<Pet> getPetsByUser(User user)
  Pet getPetByUuid(String uuid)
}
