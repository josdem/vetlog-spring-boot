package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.PetBinder
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.repository.PetRepository

@Service
class PetServiceImpl implements PetService {

  @Autowired
  PetBinder petBinder
  @Autowired
  PetRepository petRepository

  Pet save(Command command, User user){
    Pet pet = petBinder.bindPet(command)
    pet.user = user
    petRepository.save(pet)
    pet
  }

  List<Pet> getPetsByUser(User user){
    petRepository.findAllByUser(user)
  }

  Pet getPetByUuid(String uuid){
    petRepository.findByUuid(uuid)
  }

}
