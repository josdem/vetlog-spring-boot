package com.jos.dem.vetlog.unit

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetImage
import com.jos.dem.vetlog.service.PetImageService
import com.jos.dem.vetlog.service.impl.PetImageServiceImpl
import com.jos.dem.vetlog.repository.PetImageRepository

import spock.lang.Specification

class PetImageServiceSpec extends Specification {

  PetImageService service = new PetImageServiceImpl()

  PetImageRepository petImageRepository = Mock(PetImageRepository)

  def setup(){
    service.petImageRepository = petImageRepository
  }

  void "should save a pet image"(){
    given:"A pet"
      Pet pet = new Pet()
    when:"I save a pet image"
      PetImage result = service.save(pet)
    then:"I expect an image pet saved"
      result.uuid.length() == 32
      result.pet == pet
      1 * petImageRepository.save(_ as PetImage)
  }
}
