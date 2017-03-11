package com.jos.dem.vetlog.repository

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface PetRepository extends JpaRepository<Pet,Long> {
  Pet save(Pet pet)
  List<Pet> findAllByUser(User user)
}

