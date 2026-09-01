package com.josdem.vetlog.strategy

import com.josdem.vetlog.enums.VaccinationStatus
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.Vaccination
import com.josdem.vetlog.repository.VaccinationRepository
import com.josdem.vetlog.strategy.vaccination.impl.DogVaccinationStrategy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.Mockito.argThat
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.slf4j.LoggerFactory
import java.time.LocalDate

class DogVaccinationStrategyTest {
    private val log = LoggerFactory.getLogger(DogVaccinationStrategyTest::class.java)

    private lateinit var dogVaccinationStrategy: DogVaccinationStrategy

    @Mock
    private lateinit var vaccinationRepository: VaccinationRepository

    private val pet = Pet()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        dogVaccinationStrategy = DogVaccinationStrategy(vaccinationRepository)
    }

    @ParameterizedTest
    @CsvSource("3, 1", "6, 2", "8, 2", "9, 2", "12, 2")
    fun `should  save  vaccines  based  on  pet  age`(
        weeks: Int,
        times: Int,
    ) {
        log.info("Running test: saving vaccines")
        pet.birthDate = LocalDate.now().minusWeeks(weeks.toLong())

        dogVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository).saveAll(
            argThat { vaccinations: List<Vaccination> ->
                if (vaccinations.size != times) return@argThat false

                vaccinations.all { vaccination ->
                    vaccination.date == LocalDate.now() &&
                        vaccination.status == VaccinationStatus.PENDING &&
                        vaccination.pet == pet
                }
            },
        )
    }

    @Test
    fun `should save annual vaccines for dogs older than 12 weeks`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        pet.birthDate = LocalDate.now().minusWeeks(23)

        dogVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository).saveAll(
            argThat { vaccinations: List<Vaccination> ->
                vaccinations.size == 2 &&
                    vaccinations.any { it.name == "C6CV" } &&
                    vaccinations.any { it.name == "Deworming" }
            },
        )
    }

    @Test
    fun `should create C4CV 15 days after Puppy is applied`() {
        val previous = Vaccination(1L, "Puppy", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val applied = Vaccination(1L, "Puppy", LocalDate.now(), VaccinationStatus.APPLIED, pet)

        dogVaccinationStrategy.updateVaccines(listOf(previous), listOf(applied), pet)

        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "C4CV" &&
                    vaccination.date == LocalDate.now().plusDays(15) &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.pet == pet
            },
        )
    }

    @Test
    fun `should create C6CV 15 days after C4CV is applied`() {
        val previous = Vaccination(1L, "C4CV", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val applied = Vaccination(1L, "C4CV", LocalDate.now(), VaccinationStatus.APPLIED, pet)

        dogVaccinationStrategy.updateVaccines(listOf(previous), listOf(applied), pet)

        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "C6CV" &&
                    vaccination.date == LocalDate.now().plusDays(15) &&
                    vaccination.status == VaccinationStatus.NEW &&
                    vaccination.pet == pet
            },
        )
    }

    @Test
    fun `should create annual C6CV and Rabies after C6CV is applied`() {
        val previous = Vaccination(1L, "C6CV", LocalDate.now(), VaccinationStatus.PENDING, pet)
        val applied = Vaccination(1L, "C6CV", LocalDate.now(), VaccinationStatus.APPLIED, pet)

        dogVaccinationStrategy.updateVaccines(listOf(previous), listOf(applied), pet)

        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "C6CV" &&
                    vaccination.date == LocalDate.now().plusYears(1) &&
                    vaccination.status == VaccinationStatus.NEW
            },
        )
        verify(vaccinationRepository).save(
            argThat { vaccination ->
                vaccination.name == "Rabies" &&
                    vaccination.date == LocalDate.now().plusDays(15) &&
                    vaccination.status == VaccinationStatus.NEW
            },
        )
    }

    @Test
    fun `should not create next vaccine when status was not pending`() {
        val previous = Vaccination(1L, "Puppy", LocalDate.now(), VaccinationStatus.APPLIED, pet)
        val applied = Vaccination(1L, "Puppy", LocalDate.now(), VaccinationStatus.APPLIED, pet)

        dogVaccinationStrategy.updateVaccines(listOf(previous), listOf(applied), pet)

        verify(vaccinationRepository, never()).save(any())
    }
}
