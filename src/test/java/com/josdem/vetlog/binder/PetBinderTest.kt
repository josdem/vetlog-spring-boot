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

import com.josdem.vetlog.enums.PetType
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.Breed
import com.josdem.vetlog.model.PetImage
import com.josdem.vetlog.model.User
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
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import kotlin.collections.listOf
import kotlin.test.assertTrue


internal class PetBinderTest {

    private lateinit var petBinder: PetBinder

    @Mock
    private lateinit var breedRepository: BreedRepository

    @Mock
    private lateinit var vaccinationRepository: VaccinationRepository

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
        var pet = getPet()
        val result = petBinder.bindPet(pet)

        assertEquals(1L, result.id)
        assertEquals("1b211410-320b-11ed-a261-0242ac120002", result.uuid)
        assertEquals("Cremita", result.name)
        assertEquals("2021-01-17T00:00", result.birthDate.toString())
        assertTrue(result.dewormed)
        assertTrue(result.sterilized)
        assertTrue(result.vaccinated)
        assertFalse(result.images.isEmpty())
        assertEquals(5L, result.breed)
        assertEquals(1L, result.user)
        assertEquals(PetType.DOG, result.type)
    }

    @NotNull
    private fun getPet(): Pet {
        return Pet().apply {
            id = 1L
            uuid= "1b211410-320b-11ed-a261-0242ac120002"
            name = "Cremita"
            dewormed = true
            sterilized = true
            vaccinated = true
            images = listOf(PetImage())
            birthDate = LocalDateTime.of(2021, 1, 17, 0, 0)
            user = User().apply {
                id = 1L
            }
            breed = Breed().apply {
                id = 5L
                name = "Chihuahua"
                type = PetType.DOG
            }
        }
    }
}