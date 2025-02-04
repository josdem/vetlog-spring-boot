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

import com.josdem.vetlog.binder.PetBinder
import com.josdem.vetlog.command.Command
import com.josdem.vetlog.command.PetCommand
import com.josdem.vetlog.enums.PetStatus
import com.josdem.vetlog.exception.BusinessException
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.PetAdoption
import com.josdem.vetlog.model.User
import com.josdem.vetlog.repository.AdoptionRepository
import com.josdem.vetlog.repository.PetRepository
import com.josdem.vetlog.repository.UserRepository
import com.josdem.vetlog.service.impl.PetServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.Optional

internal class PetServiceTest {

    private lateinit var service: PetService

    @Mock
    private lateinit var petBinder: PetBinder

    @Mock
    private lateinit var petRepository: PetRepository

    @Mock
    private lateinit var petImageService: PetImageService

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var adoptionRepository: AdoptionRepository

    @Mock
    private lateinit var localeService: LocaleService

    @Mock
    private lateinit var vaccinationService: VaccinationService

    private var user: User? = null
    private var adopter: User? = null
    private var pet: Pet? = null
    private val pets: MutableList<Pet?> = mutableListOf()

    companion object {
        private val log = LoggerFactory.getLogger(PetServiceTest::class.java)
    }

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        user = User()
        adopter = User()
        pet = Pet()
        service = PetServiceImpl(
            petBinder,
            petRepository,
            petImageService,
            userRepository,
            adoptionRepository,
            localeService,
            vaccinationService
        )
    }

    @Test
    @DisplayName("Saving a pet")
    @Throws(IOException::class)
    fun shouldSavePet(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val command: Command = mock()
        whenever(petBinder.bindPet(command)).thenReturn(pet)
        service.save(command, user)

        verify(petRepository).save(any())
        verify(vaccinationService).save(any())
    }

    @Test
    @DisplayName("Updating a pet")
    @Throws(IOException::class)
    fun shouldUpdatePet(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        pet!!.images = mutableListOf()
        val command: PetCommand = mock()

        whenever(command.id).thenReturn(2L)
        whenever(petRepository.findById(2L)).thenReturn(Optional.of(pet!!))
        whenever(petBinder.bindPet(command)).thenReturn(pet)
        whenever(command.user).thenReturn(1L)
        whenever(command.adopter).thenReturn(3L)
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(user!!))
        whenever(userRepository.findById(3L)).thenReturn(Optional.of(adopter!!))

        service.update(command)
        Assertions.assertEquals(user, pet!!.user)
        Assertions.assertEquals(adopter, pet!!.adopter)
        verify(petImageService).attachFile(command)
        verify(petRepository).save(any())
        verify(command).images = any()
    }

    @Test
    @DisplayName("Not updating a pet due to user not found")
    @Throws(IOException::class)
    fun shouldNotUpdatePet(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        pet!!.images = mutableListOf()
        val command: PetCommand = mock()

        whenever(command.id).thenReturn(2L)
        whenever(petRepository.findById(2L)).thenReturn(Optional.of(pet!!))
        whenever(petBinder.bindPet(command)).thenReturn(pet)
        whenever(command.user).thenReturn(1L)
        whenever(command.adopter).thenReturn(3L)
        whenever(userRepository.findById(1L)).thenReturn(Optional.empty())

        Assertions.assertThrows(BusinessException::class.java) {
            service.update(command)
        }
    }

    @Test
    @DisplayName("Not update due to user not found")
    fun shouldNotUpdateDueToUserDoesNotExist(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val command: PetCommand = mock()
        whenever(petRepository.findById(2L)).thenReturn(Optional.of(pet!!))

        Assertions.assertThrows(BusinessException::class.java) {
            service.update(command)
        }
    }

    @Test
    @DisplayName("Not update due to pet not found")
    fun shouldNotUpdateDueToPetDoesNotExist(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val command: PetCommand = mock()
        Assertions.assertThrows(BusinessException::class.java) {
            service.update(command)
        }
    }

    @Test
    @DisplayName("Getting pet by uuid")
    fun shouldGetPetByUuid(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        whenever(petRepository.findByUuid("uuid")).thenReturn(Optional.of(pet!!))
        Assertions.assertEquals(pet, service.getPetByUuid("uuid"))
    }

    @Test
    @DisplayName("Getting pet by id")
    fun shouldGetPetById(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val optionalPet = Optional.of(pet!!)
        whenever(petRepository.findById(1L)).thenReturn(optionalPet)
        Assertions.assertEquals(pet, service.getPetById(1L))
    }

    @Test
    @DisplayName("Pet by id not found")
    fun shouldNotFoundPetById(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        Assertions.assertThrows(BusinessException::class.java) {
            service.getPetById(1L)
        }
    }

    @Test
    @DisplayName("Listing a pet by owner")
    fun shouldListPetsByOwner(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        whenever(petRepository.findAllByUser(user)).thenReturn(listOf(pet))
        whenever(petRepository.findAllByAdopter(user)).thenReturn(mutableListOf())
        whenever(petRepository.findAllByStatus(PetStatus.ADOPTED)).thenReturn(mutableListOf())
        Assertions.assertEquals(1, service.getPetsByUser(user).size)
    }

    @Test
    @DisplayName("Not listing pet if adopted")
    fun shouldNotListPetsIfAdopted(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        pets.add(pet)
        whenever(petRepository.findAllByUser(user)).thenReturn(pets)
        whenever(petRepository.findAllByAdopter(user)).thenReturn(mutableListOf())
        whenever(petRepository.findAllByStatus(PetStatus.ADOPTED)).thenReturn(pets)
        Assertions.assertTrue(service.getPetsByUser(user).isEmpty())
    }

    @Test
    @DisplayName("Listing pets if I am adopter")
    fun shouldListPetIfIamAdopter(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        whenever(petRepository.findAllByUser(user)).thenReturn(mutableListOf())
        whenever(petRepository.findAllByAdopter(user)).thenReturn(listOf(pet))
        whenever(petRepository.findAllByStatus(PetStatus.ADOPTED)).thenReturn(mutableListOf())
        Assertions.assertEquals(1, service.getPetsByUser(user).size)
    }

    @Test
    @DisplayName("Getting pet by status")
    fun shouldGetPetByStatus(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        whenever(petRepository.findAllByStatus(PetStatus.OWNED)).thenReturn(pets)
        Assertions.assertEquals(pets, service.getPetsByStatus(PetStatus.OWNED))
    }

    @Test
    @DisplayName("Getting pet adoption information")
    fun shouldGetPetAdoption(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val petAdoption = PetAdoption().apply {
            id = 1L
            description = "It is cute!"
        }
        val optional = Optional.of(petAdoption)
        pets.add(pet)

        whenever(adoptionRepository.findByPet(pet)).thenReturn(optional)

        service.getPetsAdoption(pets)
        Assertions.assertEquals("It is cute!", pets[0]!!.adoption.description)
    }

    @Test
    @DisplayName("Deleting a pet")
    fun shouldDeletePetById(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        whenever(petRepository.findById(1L)).thenReturn(Optional.of(pet!!))
        Assertions.assertDoesNotThrow {
            service.deletePetById(1L)
        }
        verify(vaccinationService).deleteVaccinesByPet(pet)
    }

    @Test
    @DisplayName("Not deleting a pet due to not found")
    fun shouldNotDeletePetById(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        whenever(petRepository.findById(1L)).thenReturn(Optional.empty())
        Assertions.assertThrows(BusinessException::class.java) {
            service.deletePetById(1L)
        }
    }

    @Test
    @DisplayName("Not deleting a pet due to IN_ADOPTION status")
    fun shouldNotDeletePetInAdoption(testInfo: TestInfo) {
        log.info("Running: {}", testInfo.displayName)
        val petInAdoption = Pet().apply {
            status = PetStatus.IN_ADOPTION
        }
        whenever(petRepository.findById(1L)).thenReturn(Optional.of(petInAdoption))

        Assertions.assertThrows(BusinessException::class.java) {
            service.deletePetById(1L)
        }
    }
}
