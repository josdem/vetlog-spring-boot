package com.josdem.vetlog.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.josdem.vetlog.helper.StorageOptionsHelper;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class GoogleStorageWriterTest {

    private GoogleStorageWriter googleStorageWriter;

    @Mock
    private CredentialsProvider credentialsProvider;

    @Mock
    private GcpProjectIdProvider gcpProjectIdProvider;

    @Mock
    private StorageOptionsHelper storageOptionsHelper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        googleStorageWriter = new GoogleStorageWriter(credentialsProvider, gcpProjectIdProvider, storageOptionsHelper);
    }

    @Test
    @DisplayName("Should upload to bucket")
    void shouldUploadToBucket(TestInfo testInfo) throws Exception {
        log.info(testInfo.getDisplayName());
        var inputStream = mock(InputStream.class);
        var builder = mock(StorageOptions.Builder.class);
        var credentials = mock(Credentials.class);
        var storage = mock(Storage.class);
        var storageOptions = mock(StorageOptions.class);

        when(gcpProjectIdProvider.getProjectId()).thenReturn("projectId");
        when(credentialsProvider.getCredentials()).thenReturn(credentials);

        when(storageOptionsHelper.getStorageOptions()).thenReturn(builder);
        when(builder.setProjectId("projectId")).thenReturn(builder);
        when(builder.setCredentials(credentials)).thenReturn(builder);
        when(builder.build()).thenReturn(storageOptions);
        when(builder.build().getService()).thenReturn(storage);

        googleStorageWriter.setup();
        googleStorageWriter.uploadToBucket("bucket", "fileName", inputStream, "contentType");
        verify(inputStream).readAllBytes();
    }
}
