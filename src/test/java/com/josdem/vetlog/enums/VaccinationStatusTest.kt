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

package com.josdem.vetlog.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory

class VaccinationStatusTest {
    private val vaccinationStatus = VaccinationStatus.PENDING

    private val log = LoggerFactory.getLogger(this::class.java)

    @Test
    fun `should return vaccination status`(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        assertEquals("PENDING", vaccinationStatus.name)
        assertEquals("Pending", vaccinationStatus.value)
        assertEquals(VaccinationStatus.PENDING, VaccinationStatus.getVaccinationStatusByValue("Pending"))
    }
}
