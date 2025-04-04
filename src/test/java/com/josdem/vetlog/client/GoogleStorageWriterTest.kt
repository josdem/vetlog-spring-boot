package com.josdem.vetlog.client

import com.google.api.gax.core.CredentialsProvider
import com.google.auth.Credentials
import com.google.cloud.spring.core.GcpProjectIdProvider
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.josdem.vetlog.exception.BusinessException
import com.josdem.vetlog.helper.StorageOptionsHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.io.InputStream

class GoogleStorageWriterTest {
    private lateinit var googleStorageWriter: GoogleStorageWriter

    @Mock
    private lateinit var credentialsProvider: CredentialsProvider

    @Mock
    private lateinit var gcpProjectIdProvider: GcpProjectIdProvider

    @Mock
    private lateinit var storageOptionsHelper: StorageOptionsHelper

    @Mock
    private lateinit var inputStream: InputStream

    @Mock
    private lateinit var storage: Storage

    @Mock
    private lateinit var credentials: Credentials

    @Mock
    private lateinit var storageOptions: StorageOptions

    @Mock
    private lateinit var builder: StorageOptions.Builder

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        googleStorageWriter = GoogleStorageWriter(credentialsProvider, gcpProjectIdProvider, storageOptionsHelper)
    }

    @Test
    @DisplayName("Should upload to bucket")
    fun shouldUploadToBucket(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        setExpectations()

        googleStorageWriter.setup()
        googleStorageWriter.uploadToBucket("bucket", "fileName", inputStream, "contentType")
        verify(inputStream).readAllBytes()
    }

    @Test
    @DisplayName("Not upload to bucket due to exception")
    fun shouldNotUploadToBucket(testInfo: TestInfo) {
        println("Running: ${testInfo.displayName}")
        setExpectations()

        googleStorageWriter.setup()
        `when`(inputStream.readAllBytes()).thenThrow(IllegalStateException("Error"))
        assertThrows<BusinessException> {
            googleStorageWriter.uploadToBucket("bucket", "fileName", inputStream, "contentType")
        }
    }

    private fun setExpectations() {
        `when`(gcpProjectIdProvider.projectId).thenReturn("projectId")
        `when`(credentialsProvider.credentials).thenReturn(credentials)

        `when`(storageOptionsHelper.storageOptions).thenReturn(builder)
        `when`(builder.setProjectId("projectId")).thenReturn(builder)
        `when`(builder.setCredentials(credentials)).thenReturn(builder)
        `when`(builder.build()).thenReturn(storageOptions)
        `when`(builder.build().service).thenReturn(storage)
    }
}
