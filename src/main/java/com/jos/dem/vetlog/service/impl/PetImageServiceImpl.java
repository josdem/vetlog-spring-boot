/*
Copyright 2022 Jose Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.service.impl;

import com.jos.dem.vetlog.client.GoogleStorageWriter;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetCommand;
import com.jos.dem.vetlog.model.PetImage;
import com.jos.dem.vetlog.repository.PetImageRepository;
import com.jos.dem.vetlog.service.PetImageService;
import com.jos.dem.vetlog.util.UuidGenerator;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PetImageServiceImpl implements PetImageService {

    private final PetImageRepository petImageRepository;
    private final GoogleStorageWriter googleStorageWriter;

    @Value("${bucket}")
    private String bucket;

    private PetImage save() {
        PetImage petImage = new PetImage();
        petImage.setUuid(UuidGenerator.generateUuid());
        petImageRepository.save(petImage);
        return petImage;
    }

    public void attachImage(Command command) throws IOException {
        PetCommand petCommand = (PetCommand) command;
        if (petCommand.getImage().getInputStream().available() > 0) {
            PetImage petImage = save();
            petCommand.getImages().add(petImage);
            googleStorageWriter.uploadToBucket(bucket, petImage.getUuid(), petCommand.getImage().getInputStream());
        }
    }

}
