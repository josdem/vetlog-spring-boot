package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.Pet
import  com.jos.dem.vetlog.model.PetLog
import  com.jos.dem.vetlog.command.Command

interface PetLogService {
  PetLog save(Command command)
  List<PetLog> getPetLogsByPet(Pet pet)
}
