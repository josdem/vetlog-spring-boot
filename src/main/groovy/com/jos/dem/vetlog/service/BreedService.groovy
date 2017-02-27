package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.Breed
import  com.jos.dem.vetlog.model.PetType

interface BreedService {
  List<Breed> getBreeds()
  List<Breed> getBreedsByType(PetType type)
}
