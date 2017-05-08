package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.PetAdoption
import  com.jos.dem.vetlog.command.Command

interface AdoptionService {
  PetAdoption save(Command command)
}
