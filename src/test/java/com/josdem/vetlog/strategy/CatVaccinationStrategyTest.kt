package com.josdem.vetlog.strategy

import com.josdem.vetlog.enums.VaccinationStatus
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.repository.VaccinationRepository
import com.josdem.vetlog.strategy.vaccination.impl.CatVaccinationStrategy
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
import java.time.LocalDateTime

class CatVaccinationStrategyTest {
    private val log = LoggerFactory.getLogger(DogVaccinationStrategyTest::class.java)

    private lateinit var catVaccinationStrategy: CatVaccinationStrategy

    @Mock
    private lateinit var vaccinationRepository: VaccinationRepository

    private val pet = Pet()

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        catVaccinationStrategy = CatVaccinationStrategy(vaccinationRepository)
    }

    @ParameterizedTest
    @CsvSource("9, 2", "12, 2", "13, 2", "16, 2", "23, 3")
    fun `should save vaccines based on pet age`(
        weeks: Int,
        times: Int,
    ) {
        log.info("Running test: saving vaccines")
        pet.birthDate = LocalDateTime.now().minusWeeks(weeks.toLong())

        catVaccinationStrategy.vaccinate(pet)

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
        pet.birthDate = LocalDateTime.now().minusWeeks(1)

        catVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository, never()).save(any())
    }
}
