package com.jos.dem.vetlog.repository

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetImage
import org.springframework.data.jpa.repository.JpaRepository

interface PetImageRepository extends JpaRepository<PetImage,Long> {

  PetImage save(Pet pet)

}

