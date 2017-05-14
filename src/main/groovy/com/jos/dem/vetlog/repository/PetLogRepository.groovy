package com.jos.dem.vetlog.repository

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetLog
import org.springframework.data.jpa.repository.JpaRepository

interface PetLogRepository extends JpaRepository<PetLog,Long> {

  PetLog save(PetLog petLog)
  List<PetLog> getAllByPet(Pet pet)

}

