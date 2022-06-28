/*
Copyright 2017 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.jos.dem.vetlog.service.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.model.PetImage

import com.jos.dem.vetlog.client.S3Writer
import com.jos.dem.vetlog.service.PetImageService
import com.jos.dem.vetlog.repository.PetImageRepository
import com.jos.dem.vetlog.util.UuidGenerator

@Service
class PetImageServiceImpl implements PetImageService {

  @Autowired
  S3Writer s3Writer
  @Autowired
  PetImageRepository petImageRepository

  @Value('${bucketDestination}')
  String bucketDestination

  private PetImage save(){
    PetImage petImage = new PetImage(uuid:UuidGenerator.generateUuid())
    petImageRepository.save(petImage)
    petImage
  }

  void attachImage(Command command){
    if(command.image.getInputStream().available() > 0){
      PetImage petImage = save()
      command.images.add(petImage)
      s3Writer.uploadToBucket(bucketDestination, petImage.uuid, command.image.getInputStream())
    }
  }

}
