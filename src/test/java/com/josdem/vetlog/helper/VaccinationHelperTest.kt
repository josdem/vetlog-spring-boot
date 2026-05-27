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

import com.josdem.vetlog.enums.PetType
import com.josdem.vetlog.enums.VaccinationStatus
import com.josdem.vetlog.model.Breed
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.Vaccination
import com.josdem.vetlog.repository.VaccinationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.never
import org.mockito.kotlin.times
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

        val expectedDate = LocalDate.now().plusYears(1)
        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "Rabies" &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.date == expectedDate &&
                    vaccination.pet == pet
            },
        )
    }

    @Test
    fun `should update c6cv vaccination status to APPLIED`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val previousVaccines = Vaccination(2L, "C6CV", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(2L, "C6CV", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))
        vaccinationHelper.validateRabiesVaccine(listOf(previousVaccines), listOf(newVaccines), pet)
        val expectedDate = LocalDate.now().plusDays(15)
        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "Rabies" &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.date == expectedDate &&
                    vaccination.pet == pet
            },
        )
    }

    @Test
    fun `should update every c6cv, tricat_boost and rabies vaccination status to APPLIED`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val previousVaccines =
            listOf(
                Vaccination(1L, "C6CV", LocalDate.now(), VaccinationStatus.PENDING, pet),
                Vaccination(2L, "Rabies", LocalDate.now(), VaccinationStatus.PENDING, pet),
                Vaccination(3L, "TRICAT_BOOST", LocalDate.now(), VaccinationStatus.PENDING, pet),
            )
        val newVaccines =
            listOf(
                Vaccination(1L, "C6CV", LocalDate.now(), VaccinationStatus.APPLIED, pet),
                Vaccination(2L, "Rabies", LocalDate.now(), VaccinationStatus.APPLIED, pet),
                Vaccination(3L, "TRICAT_BOOST", LocalDate.now(), VaccinationStatus.APPLIED, pet),
            )
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(previousVaccines)
        vaccinationHelper.validateRabiesVaccine(
            previousVaccines,
            newVaccines,
            pet,
        )
        val offsets =
            listOf(
                java.time.Period.ofDays(15),
                java.time.Period.ofYears(1),
                java.time.Period.ofDays(21),
            )
        val baseDate = previousVaccines.first().date
        offsets.forEach { p ->
            verify(vaccinationRepository).save(
                argThat { vaccination ->
                    vaccination.name == "Rabies" &&
                        vaccination.status == VaccinationStatus.NEW &&
                        vaccination.date == baseDate.plus(p) &&
                        vaccination.pet == pet
                },
            )
        }
    }

    @Test
    fun `should update puppy vaccination status to APPLIED`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val previousVaccines = Vaccination(1L, "Puppy", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "Puppy", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateNextVaccines(listOf(previousVaccines), listOf(newVaccines), pet)

        val expectedDate = LocalDate.now().plusDays(15)
        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "C4CV" &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.date == expectedDate &&
                    vaccination.pet == pet
            },
        )
    }

    @Test
    fun `should create C6CV 15 days later when C4CV applied`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val previousVaccines = Vaccination(1L, "C4CV", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "C4CV", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateNextVaccines(listOf(previousVaccines), listOf(newVaccines), pet)

        val expectedDate = LocalDate.now().plusDays(15)
        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "C6CV" &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.date == expectedDate &&
                    vaccination.pet == pet
            },
        )
    }

    @Test
    fun `should create TRICAT_BOOST 21 days later when TRICAT applied and pet is a cat aged 9 to 16 weeks`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pet = Pet()
        val breed = Breed()
        breed.type = PetType.CAT
        pet.breed = breed
        pet.birthDate = LocalDate.now().minusWeeks(9)
        val previousVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateNextVaccines(listOf(previousVaccines), listOf(newVaccines), pet)

        val expectedDate = LocalDate.now().plusDays(21)
        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "TRICAT_BOOST" &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.date == expectedDate &&
                    vaccination.pet == pet
            },
        )
    }

    @Test
    fun `should not create TRICAT_BOOST when TRICAT applied and pet is cat not aged 9 to 16 weeks`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pet = Pet()
        val breed = Breed()
        breed.type = PetType.CAT
        pet.breed = breed
        pet.birthDate = LocalDate.now().minusWeeks(5)
        val previousVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateNextVaccines(listOf(previousVaccines), listOf(newVaccines), pet)

        verify(vaccinationRepository, times(0)).save(
            argThat { vaccination ->
                vaccination.name == "TRICAT_BOOST" &&
                    vaccination.status == VaccinationStatus.NEW
            },
        )
    }

    @Test
    fun `should create Rabies 21 days later when TRICAT applied and pet is older than 16 weeks`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pet = Pet()
        pet.birthDate = LocalDate.now().minusWeeks(20)
        val previousVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateRabiesVaccine(listOf(previousVaccines), listOf(newVaccines), pet)

        val expectedDate = LocalDate.now().plusDays(21)
        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "Rabies" &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.date == expectedDate &&
                    vaccination.pet == pet
            },
        )
    }

    @Test
    fun `should not create Rabies when TRICAT applied and pet is 16 weeks or younger`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pet = Pet()
        pet.birthDate = LocalDate.now().minusWeeks(12)
        val previousVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateRabiesVaccine(listOf(previousVaccines), listOf(newVaccines), pet)

        verify(vaccinationRepository, times(0)).save(any())
    }

    @Test
    fun `should not create Rabies when pet is exactly 16 weeks old`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pet = Pet()
        pet.birthDate = LocalDate.now().minusWeeks(16)
        val previousVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateRabiesVaccine(listOf(previousVaccines), listOf(newVaccines), pet)

        verify(vaccinationRepository, never()).save(any())
    }

    @Test
    fun `should create Rabies when pet is older than 16 weeks by one day`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val today = LocalDate.now()
        val pet = Pet()
        pet.birthDate = today.minusWeeks(16).minusDays(1)
        val previousVaccines = Vaccination(1L, "TRICAT", today, VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "TRICAT", today, VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))
        vaccinationHelper.validateRabiesVaccine(listOf(previousVaccines), listOf(newVaccines), pet)

        verify(vaccinationRepository).save(any())
    }

    @Test
    fun `should update TRICAT_BOOST vaccination status to APPLIED`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val previousVaccines = Vaccination(3L, "TRICAT_BOOST", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(3L, "TRICAT_BOOST", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))
        vaccinationHelper.validateRabiesVaccine(listOf(previousVaccines), listOf(newVaccines), pet)
        val expectedDate = LocalDate.now().plusDays(21)
        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "Rabies" &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.date == expectedDate &&
                    vaccination.pet == pet
            },
        )
    }

    @Test
    fun `should create new FeLV vaccine when TRICAT is applied`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pet = Pet()
        pet.birthDate = LocalDate.now().minusWeeks(16).minusDays(1)
        pet.goingOutOften = true
        pet.breed = Breed().apply { type = PetType.CAT }
        val previousVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateRabiesVaccine(listOf(previousVaccines), listOf(newVaccines), pet)

        verify(vaccinationRepository).save(any())
    }

    @Test
    fun `should not create new FELV vaccine when cat is 16 months old or younger`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pet = Pet()
        pet.id = 1L
        pet.birthDate = LocalDate.now().minusWeeks(16)
        pet.goingOutOften = true
        pet.breed = Breed().apply { type = PetType.CAT }
        val previousVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val newVaccines = Vaccination(1L, "TRICAT", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        whenever(vaccinationRepository.findAllByPetId(1L)).thenReturn(listOf(previousVaccines))

        vaccinationHelper.validateRabiesVaccine(listOf(previousVaccines), listOf(newVaccines), pet)

        verify(vaccinationRepository, never()).save(
            argThat { vaccination ->
                vaccination.name == "FeLV" &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.pet == pet
            },
        )
    }
}
