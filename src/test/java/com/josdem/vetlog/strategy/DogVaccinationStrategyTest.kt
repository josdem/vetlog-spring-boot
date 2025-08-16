package com.josdem.vetlog.strategy

import com.josdem.vetlog.enums.VaccinationStatus
import com.josdem.vetlog.model.Pet
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
    @CsvSource("6, 2", "8, 2", "9, 2", "12, 2", "20, 3")
    fun `should  save  vaccines  based  on  pet  age`(
        weeks: Int,
        times: Int,
    ) {
        log.info("Running test: saving vaccines")
        pet.birthDate = LocalDate.now().minusWeeks(weeks.toLong())

        dogVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository, times(times))
            .save(
                argThat { vaccination ->
                    vaccination.date == LocalDate.now() &&
                        vaccination.status == VaccinationStatus.PENDING &&
                        vaccination.pet == pet
                },
            )
    }

    @Test
    fun `should not save vaccination when pet is not old enough`(testInfo: TestInfo) {
        log.info("Running test: {}", testInfo.displayName)
        pet.birthDate = LocalDate.now().minusWeeks(1)

        dogVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository, never()).save(any())
    }

    @ParameterizedTest
    @CsvSource("13, 3", "16, 3", "20, 3", "52, 3")
    fun `should only register expected vaccines for pets older than 12 weeks`(
        weeks: Int,
        times: Int,
    ) {
        pet.birthDate = LocalDate.now().minusWeeks(weeks.toLong())

        dogVaccinationStrategy.vaccinate(pet)

        val expectedVaccines = setOf("DA2PP", "Deworming", "Rabies")

        verify(vaccinationRepository, times(times)).save(
            argThat { vaccination ->
                vaccination.pet == pet && vaccination.name in expectedVaccines
            },
        )
    }
}
