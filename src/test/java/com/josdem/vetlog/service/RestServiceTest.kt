package com.josdem.vetlog.service

import com.josdem.vetlog.command.MessageCommand
import com.josdem.vetlog.service.impl.RestServiceImpl
import okhttp3.ResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInfo
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Retrofit
import java.io.IOException
import kotlin.test.Test

internal class RestServiceTest {
    private lateinit var service: RestServiceImpl

    @Mock
    private lateinit var retrofit: Retrofit

    companion object {
        private val log = LoggerFactory.getLogger(RestServiceTest::class.java)
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        service = RestServiceImpl(retrofit)
    }

    @Test
    @DisplayName("Sending message")
    @Throws(IOException::class)
    fun shouldSendMessage(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val restService: RestService = mock()
        val messageCommand: MessageCommand = mock()
        val call: Call<ResponseBody> = mock()

        whenever(retrofit.create(RestService::class.java)).thenReturn(restService)
        whenever(restService.sendMessage(messageCommand)).thenReturn(call)

        service.setup()
        service.sendMessage(messageCommand)

        verify(call).execute()
    }
}