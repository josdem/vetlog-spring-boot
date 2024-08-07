/*
Copyright 2024 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.josdem.vetlog.client.GoogleStorageWriter;
import com.josdem.vetlog.command.PetCommand;
import com.josdem.vetlog.model.PetImage;
import com.josdem.vetlog.repository.PetImageRepository;
import com.josdem.vetlog.service.impl.PetImageServiceImpl;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
class PetImageServiceTest {

    private PetImageService service;

    @Mock
    private PetImageRepository petImageRepository;

    @Mock
    private GoogleStorageWriter googleStorageWriter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new PetImageServiceImpl(petImageRepository, googleStorageWriter);
    }

    @Test
    @DisplayName("saving a pet image")
    void shouldSavePetImage(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());
        PetCommand petCommand = new PetCommand();
        MultipartFile multiPartFile = mock(MultipartFile.class);
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.available()).thenReturn(1000);
        when(multiPartFile.getInputStream()).thenReturn(inputStream);
        petCommand.setImage(multiPartFile);

        service.attachFile(petCommand);

        verify(petImageRepository).save(Mockito.isA(PetImage.class));
        verify(googleStorageWriter).uploadToBucket(any(), anyString(), eq(inputStream), anyString());
    }
}
