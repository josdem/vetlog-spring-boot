package com.josdem.vetlog.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertTrue

@SpringBootTest
internal class LocationServiceTest {
    @Autowired
    private lateinit var locationService: LocationService

    private val log = LoggerFactory.getLogger(this::class.java)

    @Test
    fun `should get geolocation`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val call = locationService.getLocation(1L)
        assertTrue { call.isExecuted }
    }
}
