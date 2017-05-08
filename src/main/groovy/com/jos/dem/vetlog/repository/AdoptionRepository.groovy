package com.jos.dem.vetlog.repository

import com.jos.dem.vetlog.model.PetAdoption
import org.springframework.data.jpa.repository.JpaRepository

interface AdoptionRepository extends JpaRepository<PetAdoption,Long> {

  PetAdoption save(PetAdoption petAdoption)

}

