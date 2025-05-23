/*
  Copyright 2025 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.service

import com.josdem.vetlog.command.MobileCommand
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "GOOGLE_API_KEY", matches = ".+")
internal class GoogleServiceTest {
    @Autowired
    private lateinit var googleService: GoogleService

    private val log = LoggerFactory.getLogger(this::class.java)

    @Test
    fun `should get geolocation`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val call = googleService.getGeolocation(System.getenv("GOOGLE_API_KEY"), mobileCommand)
        val execute = call.execute()
        val result = execute.body()
        log.info("result: $result")
        assertNotNull(result?.location)
        assertNotNull(result.accuracy)
    }

    val mobileCommand =
        MobileCommand().apply {
            homeMobileCountryCode = 310
            homeMobileNetworkCode = 410
            radioType = "GSM"
            carrier = "AT&T"
            isConsiderIp = true
        }
}
