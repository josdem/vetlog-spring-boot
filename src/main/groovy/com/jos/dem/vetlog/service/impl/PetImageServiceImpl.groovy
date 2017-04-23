package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetLog
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.binder.PetLogBinder
import com.jos.dem.vetlog.service.PetLogService
import com.jos.dem.vetlog.repository.PetRepository
import com.jos.dem.vetlog.repository.PetLogRepository
import com.jos.dem.vetlog.util.UuidGenerator

@Service
class PetImageServiceImpl implements PetImageService {

  @Autowired
  PetImageRepository petImageRepository

  PetImage save(Pet pet){
    PetImage petImage = new PetImage(uuid:UuidGenerator.generateUuid())
    petImage.pet = pet
    petImageRepository.save(petLog)
    petImage
  }

}
