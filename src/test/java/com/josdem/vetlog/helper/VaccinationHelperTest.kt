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
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.Vaccination
import com.josdem.vetlog.repository.VaccinationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.test.Test

class VaccinationHelperTest {
    private val pet = Pet()

    private lateinit var vaccinationHelper: VaccinationHelper

    private val log = LoggerFactory.getLogger(VaccinationHelperTest::class.java)

    @Mock
    private lateinit var vaccinationRepository: VaccinationRepository

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        vaccinationHelper = VaccinationHelper(vaccinationRepository)
    }

    @Test
    fun `should update rabies vaccination status to APPLIED`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val previousVaccines = Vaccination(1L, "Rabies", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "Rabies", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateRabiesVaccine(listOf(previousVaccines), listOf(newVaccines), pet)

        verify(vaccinationRepository).save(any())
    }

    @Test
    fun `should update puppy vaccination status to APPLIED`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val previousVaccines = Vaccination(1L, "Puppy", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "Puppy", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validatePuppyVaccines(listOf(previousVaccines), listOf(newVaccines), pet)

        verify(vaccinationRepository).save(any())
    }
}
