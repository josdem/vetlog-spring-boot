package com.jos.dem.vetlog.repository

import com.jos.dem.vetlog.model.Breed
import org.springframework.data.jpa.repository.JpaRepository

interface BreedRepository extends JpaRepository<Breed,Long> {

  Breed findOne(Long breed)
  Breed save(Breed breed)
  List<Breed> findAll()

}

