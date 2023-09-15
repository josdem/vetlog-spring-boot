package com.jos.dem.vetlog.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jos.dem.vetlog.client.GoogleStorageWriter;
import com.jos.dem.vetlog.command.PetCommand;
import com.jos.dem.vetlog.model.PetImage;
import com.jos.dem.vetlog.repository.PetImageRepository;
import com.jos.dem.vetlog.service.impl.PetImageServiceImpl;
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
