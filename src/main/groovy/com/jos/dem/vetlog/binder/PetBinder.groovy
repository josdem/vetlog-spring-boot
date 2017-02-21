package com.jos.dem.vetlog.binder

import org.springframework.stereotype.Component
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.command.Command

@Component
class PetBinder {

  Pet bindPet(Command command){
    Pet pet = new Pet()
    pet
  }

}
