package com.jos.dem.vetlog.repository

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.enums.PetStatus
import org.springframework.data.jpa.repository.JpaRepository

interface PetRepository extends JpaRepository<Pet,Long> {
  Pet save(Pet pet)
  Pet findByUuid(String uuid)
  List<Pet> findAllByUser(User user)
  List<Pet> findAllByStatus(PetStatus status)
}

