package com.jos.dem.vetlog.service

import  com.jos.dem.vetlog.model.Pet
import  com.jos.dem.vetlog.model.PetImage

interface PetImageService {
  PetImage save(Pet pet)
}
