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

import com.josdem.vetlog.command.PetCommand
import com.josdem.vetlog.enums.PetStatus
import com.josdem.vetlog.enums.PetType
import com.josdem.vetlog.enums.VaccinationStatus
import com.josdem.vetlog.exception.BusinessException
import com.josdem.vetlog.model.Breed
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.Vaccination
import com.josdem.vetlog.repository.VaccinationRepository
import com.josdem.vetlog.service.impl.VaccinationServiceImpl
import com.josdem.vetlog.strategy.vaccination.VaccinationStrategy
import com.josdem.vetlog.strategy.vaccination.impl.CatVaccinationStrategy
import com.josdem.vetlog.strategy.vaccination.impl.DogVaccinationStrategy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import java.time.LocalDate
import kotlin.test.Test

internal class VaccinationServiceTest {
    private lateinit var vaccinationService: VaccinationService

    @Mock
    private lateinit var vaccinationRepository: VaccinationRepository

    private val pet = Pet()

    companion object {
        private val log = LoggerFactory.getLogger(VaccinationServiceTest::class.java)
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        val dogVaccinationStrategy = DogVaccinationStrategy(vaccinationRepository)
        val catVaccinationStrategy = CatVaccinationStrategy(vaccinationRepository)

        val vaccinationStrategies =
            mutableMapOf<PetType, VaccinationStrategy>(
                PetType.DOG to dogVaccinationStrategy,
                PetType.CAT to catVaccinationStrategy,
            )

        vaccinationService = VaccinationServiceImpl(vaccinationRepository, vaccinationStrategies)
        pet.breed = Breed()
    }

    @Test
    fun `Not saving a pet if it is not a dog or cat`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        pet.breed.type = PetType.BIRD
        assertThrows<BusinessException> { vaccinationService.save(pet) }
    }

    @ParameterizedTest
    @CsvSource("7, 2", "10, 2", "20, 3")
    fun `Saving vaccines`(
        weeks: Int,
        times: Int,
    ) {
        log.info("Running test: Saving vaccines")
        pet.breed.type = PetType.DOG
        pet.birthDate = LocalDate.now().minusWeeks(weeks.toLong())
        vaccinationService.save(pet)
        verify(vaccinationRepository, times(times)).save(any())
    }

    @Test
    fun `Not saving vaccination due to not being old enough`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        pet.breed.type = PetType.DOG
        pet.birthDate = LocalDate.now().minusWeeks(1)
        vaccinationService.save(pet)
        verify(vaccinationRepository, never()).save(any())
    }

    @Test
    fun `Getting vaccines in Pending status`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(vaccinationRepository.findAllByPet(pet)).thenReturn(
            listOf(
                Vaccination(1L, "C6CV", LocalDate.now(), VaccinationStatus.PENDING, pet),
                Vaccination(2L, "Deworming", LocalDate.now(), VaccinationStatus.APPLIED, pet),
            ),
        )
        val vaccinesInPendingStatus = vaccinationService.getVaccinesByStatus(pet, VaccinationStatus.PENDING)
        assertEquals(1, vaccinesInPendingStatus.size)
    }

    @Test
    fun `Deleting vaccines`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        vaccinationService.deleteVaccinesByPet(pet)
        verify(vaccinationRepository).deleteAllByPet(pet)
    }

    @Test
    fun `should update rabies vaccination status to APPLIED`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val rabiesVaccination = Vaccination(1L, "Rabies", LocalDate.now(), VaccinationStatus.PENDING, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(rabiesVaccination))

        vaccinationService.updateVaccinations(getPetCommand(), pet)

        verify(vaccinationRepository).save(any())
    }

    private fun getPetCommand() =
        PetCommand().apply {
            id = 1L
            name = "Cremita"
            breed = 5L
            status = PetStatus.OWNED
            sterilized = true
            type = PetType.DOG
            vaccines = listOf(Vaccination(1L, "Rabies", LocalDate.now(), VaccinationStatus.APPLIED, pet))
        }
}
