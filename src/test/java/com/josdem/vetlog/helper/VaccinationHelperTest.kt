/*
  Copyright 2026 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.helper

import com.josdem.vetlog.enums.VaccinationStatus
import com.josdem.vetlog.exception.BusinessException
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.Vaccination
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import java.time.LocalDate

class VaccinationHelperTest {
    val pet = Pet()
    private lateinit var vaccinationHelper: VaccinationHelper

    private val log = LoggerFactory.getLogger(VaccinationHelperTest::class.java)

    @BeforeEach
    fun setup() {
        vaccinationHelper = VaccinationHelper()
    }

    @Test
    fun `should throw an exception if new vaccination is scheduled by seven months`(testInfo: TestInfo) {
        log.info(testInfo.displayName)

        val vaccinationDate = LocalDate.now().plusMonths(7)
        val newVaccines = listOf(Vaccination(1L, "Rabies", vaccinationDate, VaccinationStatus.APPLIED, pet))

        assertThrows<BusinessException> {
            vaccinationHelper.validateVaccinationDate(newVaccines)
        }
    }

    @Test
    fun `should not throw an exception if new vaccination is scheduled by one week`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val vaccinationDate = LocalDate.now().plusWeeks(1)
        val newVaccines = listOf(Vaccination(1L, "Rabies", vaccinationDate, VaccinationStatus.APPLIED, pet))

        assertDoesNotThrow { vaccinationHelper.validateVaccinationDate(newVaccines) }
    }
}
