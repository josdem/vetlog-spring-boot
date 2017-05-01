package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.PetImage
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.service.PetImageService
import com.jos.dem.vetlog.repository.PetImageRepository
import com.jos.dem.vetlog.util.UuidGenerator

@Service
class PetImageServiceImpl implements PetImageService {

  @Autowired
  PetImageRepository petImageRepository

  PetImage save(){
    PetImage petImage = new PetImage(uuid:UuidGenerator.generateUuid())
    petImageRepository.save(petImage)
    petImage
  }

}
