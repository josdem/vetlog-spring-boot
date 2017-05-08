package com.jos.dem.vetlog.binder

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.util.UuidGenerator
import com.jos.dem.vetlog.repository.BreedRepository

@Component
class PetBinder {

  @Autowired
  BreedRepository breedRepository


  Pet bindPet(Command command){
    Pet pet = new Pet()
    pet.uuid = UuidGenerator.generateUuid()
    pet.name = command.name
    pet.birthDate = command.birthDate
    pet.dewormed = command.dewormed
    pet.sterilized = command.sterilized
    pet.vaccinated = command.vaccinated
    pet.images = command.images
    pet.status = PetStatus.OWNED
    pet.breed = breedRepository.findOne(command.breed)
    pet
  }

}
