package com.jos.dem.vetlog.repository

import com.jos.dem.vetlog.model.Pet
import org.springframework.data.jpa.repository.JpaRepository

interface PetRepository extends JpaRepository<Pet,Long> {

  Pet save(Pet pet)

}

