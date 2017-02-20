package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.UserBinder
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.repository.UserRepository

@Service
class PetServiceImpl implements PetService {

  @Autowired
  PetBinder petBinder
  @Autowired
  PetRepository petRepository

  Pet save(Command command){
    Pet pet = petBinder.bindUser(command)
    petRepository.save(pet)
    pet
  }

}
