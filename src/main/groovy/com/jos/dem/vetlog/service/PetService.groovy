package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.Pet
import  com.jos.dem.vetlog.model.User
import  com.jos.dem.vetlog.enums.PetStatus
import  com.jos.dem.vetlog.command.Command

interface PetService {
  Pet save(Command command, User user)
  Pet getPetByUuid(String uuid)
  List<Pet> getPetsByUser(User user)
  List<Pet> getPetsByStatus(PetStatus status)
}
