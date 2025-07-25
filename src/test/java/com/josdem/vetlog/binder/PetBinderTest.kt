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

package com.josdem.vetlog.binder

import com.josdem.vetlog.command.PetCommand
import com.josdem.vetlog.enums.PetStatus
import com.josdem.vetlog.enums.PetType
import com.josdem.vetlog.enums.VaccinationStatus
import com.josdem.vetlog.model.Breed
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.PetImage
import com.josdem.vetlog.model.User
import com.josdem.vetlog.model.Vaccination
import com.josdem.vetlog.repository.BreedRepository
import com.josdem.vetlog.repository.VaccinationRepository
import com.josdem.vetlog.service.AdoptionServiceTest
import org.jetbrains.annotations.NotNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class PetBinderTest {
    private lateinit var petBinder: PetBinder

    @Mock
    private lateinit var breedRepository: BreedRepository

    @Mock
    private lateinit var vaccinationRepository: VaccinationRepository

    private val vaccines =
        listOf(
            Vaccination(1L, "DA2PP", LocalDate.now(), VaccinationStatus.APPLIED, null),
            Vaccination(2L, "Deworming", LocalDate.now(), VaccinationStatus.PENDING, null),
        )

    companion object {
        private val log = LoggerFactory.getLogger(AdoptionServiceTest::class.java)
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        petBinder = PetBinder(breedRepository, vaccinationRepository)
    }

    @Test
    fun `should bind a pet`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pet = getPet(uuid = UUID.randomUUID().toString())
        val result = petBinder.bindPet(pet)

        assertEquals(1L, result.id)
        assertEquals(pet.uuid, result.uuid)
        assertEquals("Cremita", result.name)
        assertEquals("2021-01-17", result.birthDate)
        assertTrue(result.sterilized)
        assertFalse(result.images.isEmpty())
        assertEquals(5L, result.breed)
        assertEquals(1L, result.user)
        assertEquals(PetType.DOG, result.type)
    }

    @Test
    fun `binding a pet from command`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        var petCommand = getPetCommand()
        petCommand.birthDate = "2021-01-17"
        setBreedExpectations()

        val result = petBinder.bindPet(petCommand)

        assertEquals(2L, result.id)
        assertEquals(36, result.uuid.length)
        assertEquals("Marla", result.name)
        assertEquals(PetStatus.IN_ADOPTION, result.status)
        assertNotNull(result.images)
        assertEquals(1L, result.breed.id)
        vaccines.forEach {
            assertEquals(LocalDate.now(), it.date)
        }
    }

    @Test
    fun `binding a pet from command even without birthdate`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        var petCommand = getPetCommand()
        petCommand.birthDate = ""
        setBreedExpectations()

        val result = petBinder.bindPet(petCommand)

        val diff: Int = LocalDateTime.now().dayOfYear - result.birthDate.dayOfYear
        assertEquals(0, diff)
    }

    private fun setBreedExpectations() {
        whenever(breedRepository.findById(1L)).thenReturn(
            Optional.of(
                Breed().apply {
                    id = 1L
                    name = "Chihuahua"
                    type = PetType.DOG
                },
            ),
        )
    }

    private fun getPetCommand(): PetCommand =
        PetCommand().apply {
            id = 2L
            name = "Marla"
            status = PetStatus.IN_ADOPTION
            sterilized = true
            images = listOf(PetImage())
            breed = 1L
            vaccines = vaccines
        }

    @NotNull
    private fun getPet(uuid: String): Pet =
        Pet().apply {
            id = 1L
            this.uuid = uuid
            name = "Cremita"
            sterilized = true
            images = listOf(PetImage())
            birthDate = LocalDate.of(2021, 1, 17)
            user =
                User().apply {
                    id = 1L
                }
            breed =
                Breed().apply {
                    id = 5L
                    name = "Chihuahua"
                    type = PetType.DOG
                }
        }

    @Test
    fun `should create a new rabies vaccination with NEW status one year later when updated to APPLIED`(testInfo: TestInfo) {
        log.info(testInfo.displayName)

        val previousRabies = Vaccination(99L, "Rabies", LocalDate.now(), VaccinationStatus.PENDING, null)

        val appliedRabies = Vaccination(null, "Rabies", LocalDate.now(), VaccinationStatus.APPLIED, null)

        val petCommand =
            PetCommand().apply {
                id = 1L
                name = "Luna"
                status = PetStatus.OWNED
                sterilized = false
                images = listOf(PetImage())
                breed = 1L
                vaccines = mutableListOf(appliedRabies)
                birthDate = "2021-01-01"
            }

        setBreedExpectations()

        whenever(vaccinationRepository.findAllByPet(any())).thenReturn(listOf(previousRabies))

        val savedVaccinations = mutableListOf<Vaccination>()
        whenever(vaccinationRepository.save(any())).thenAnswer {
            val saved = it.arguments[0] as Vaccination
            savedVaccinations.add(saved)
            saved
        }

        val result = petBinder.bindPet(petCommand)

        val futureRabies = savedVaccinations.find { it.name == "Rabies" && it.status == VaccinationStatus.NEW }
        assertNotNull(futureRabies)
        assertEquals(LocalDate.now().plusYears(1), futureRabies.date)
    }
}
