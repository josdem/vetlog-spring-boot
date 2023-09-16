package com.jos.dem.vetlog.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jos.dem.vetlog.client.GoogleStorageWriter;
import com.jos.dem.vetlog.command.PetLogCommand;
import com.jos.dem.vetlog.service.impl.PetPrescriptionServiceImpl;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
class PetPrescriptionServiceTest {

    private PetPrescriptionService service;

    @Mock
    private GoogleStorageWriter googleStorageWriter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new PetPrescriptionServiceImpl(googleStorageWriter);
    }

    @Test
    @DisplayName("saving a pet image")
    void shouldSavePetImage(TestInfo testInfo) throws IOException {
        log.info("Running: {}", testInfo.getDisplayName());
        PetLogCommand petLogCommand = new PetLogCommand();
        MultipartFile multiPartFile = mock(MultipartFile.class);
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.available()).thenReturn(1000);
        when(multiPartFile.getInputStream()).thenReturn(inputStream);
        petLogCommand.setAttachment(multiPartFile);

        service.attachFile(petLogCommand);

        verify(googleStorageWriter).uploadToBucket(any(), anyString(), eq(inputStream), anyString());
    }
}
