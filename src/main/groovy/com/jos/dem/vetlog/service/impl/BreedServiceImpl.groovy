package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Breed
import com.jos.dem.vetlog.service.BreedService
import com.jos.dem.vetlog.repository.BreedRepository

@Service
class BreedServiceImpl implements BreedService {

  @Autowired
  BreedRepository breedRepository

  List<Breed> getBreeds(){
    breedRepository.findAll()
  }

}
