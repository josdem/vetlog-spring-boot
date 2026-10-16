package com.josdem.vetlog.service
import com.josdem.vetlog.command.MobileCommand
import com.josdem.vetlog.model.GoogleResponse
import com.josdem.vetlog.service.impl.GoogleServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Call
import retrofit2.Retrofit

internal class GoogleServiceImplTest {
    private lateinit var service: GoogleService

    @Mock
    private lateinit var googleRetrofit: Retrofit

    @Mock
    private lateinit var googleService: GoogleService

    @Mock
    private lateinit var call: Call<GoogleResponse>

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        service = GoogleServiceImpl(googleRetrofit)
    }

    @Test
    fun `should get geolocation`() {
        val key = "api-key"
        val command = MobileCommand()

        whenever(googleRetrofit.create(GoogleService::class.java))
            .thenReturn(googleService)

        whenever(googleService.getGeolocation(key, command))
            .thenReturn(call)
        val result = service.getGeolocation(key, command)

        verify(googleRetrofit).create(GoogleService::class.java)
        verify(googleService).getGeolocation(key, command)
        assertEquals(call, result)
    }

    @Test
    fun `should create Google service only once`() {
        val key = "api-key"
        val command = MobileCommand()

        whenever(googleRetrofit.create(GoogleService::class.java))
            .thenReturn(googleService)

        whenever(googleService.getGeolocation(key, command))
            .thenReturn(call)

        service.getGeolocation(key, command)
        service.getGeolocation(key, command)

        verify(googleRetrofit, times(1))
            .create(GoogleService::class.java)

        verify(googleService, times(2))
            .getGeolocation(key, command)
    }
}
