/*
Copyright 2024 Jose Morales contact@josdem.io

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
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

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

        val vaccinationStrategies = mutableMapOf<PetType, VaccinationStrategy>(
            PetType.DOG to dogVaccinationStrategy,
            PetType.CAT to catVaccinationStrategy
        )

        vaccinationService = VaccinationServiceImpl(vaccinationRepository, vaccinationStrategies)
        pet.breed = Breed()
    }

    @Test
    @DisplayName("Not saving a pet if it is not a dog or cat")
    fun shouldNotSavePetIfItIsNotADogOrCat(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        pet.breed.type = PetType.BIRD
        assertThrows<BusinessException> { vaccinationService.save(pet) }
    }

    @DisplayName("Saving vaccines")
    @ParameterizedTest
    @CsvSource("9, 2", "13, 3", "20, 5")
    fun shouldSaveVaccines(weeks: Int, times: Int) {
        log.info("Test: saving vaccines")
        pet.breed.type = PetType.DOG
        pet.birthDate = LocalDateTime.now().minusWeeks(weeks.toLong())
        vaccinationService.save(pet)
        verify(vaccinationRepository, times(times)).save(any())
    }

    @Test
    @DisplayName("Not saving vaccination due to not being old enough")
    fun shouldNotSaveVaccinationDueToNotOldEnough(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        pet.breed.type = PetType.DOG
        pet.birthDate = LocalDateTime.now().minusWeeks(1)
        vaccinationService.save(pet)
        verify(vaccinationRepository, never()).save(any())
    }

    @Test
    @DisplayName("Getting vaccines in Pending status")
    fun shouldGetVaccinesInPendingStatus(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        whenever(vaccinationRepository.findAllByPet(pet)).thenReturn(
            listOf(
                Vaccination(1L, "DA2PP", LocalDate.now(), VaccinationStatus.PENDING, pet),
                Vaccination(2L, "Deworming", LocalDate.now(), VaccinationStatus.APPLIED, pet)
            )
        )
        val vaccinesInPendingStatus = vaccinationService.getVaccinesByStatus(pet, VaccinationStatus.PENDING)
        Assertions.assertEquals(1, vaccinesInPendingStatus.size)
    }

    @Test
    @DisplayName("Deleting vaccines")
    fun shouldDeleteVaccines(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        vaccinationService.deleteVaccinesByPet(pet)
        verify(vaccinationRepository).deleteAllByPet(pet)
    }
}

