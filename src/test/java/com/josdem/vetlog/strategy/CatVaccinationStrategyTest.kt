package com.josdem.vetlog.strategy

import com.josdem.vetlog.enums.VaccinationStatus
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.Vaccination
import com.josdem.vetlog.repository.VaccinationRepository
import com.josdem.vetlog.strategy.vaccination.impl.CatVaccinationStrategy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mock
import org.mockito.Mockito.argThat
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.slf4j.LoggerFactory
import java.time.LocalDate

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
    @CsvSource("9, 5", "12, 5", "13, 5", "16, 5", "23, 4")
    fun `should save vaccines based on pet age`(
        weeks: Int,
        times: Int,
    ) {
        log.info("Running test: saving vaccines")
        pet.birthDate = LocalDate.now().minusWeeks(weeks.toLong())

        catVaccinationStrategy.vaccinate(pet)

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
    fun `should apply deworming vaccine for kittens`() {
        log.info("Running test: should apply deworming vaccine for kittens")
        pet.birthDate = LocalDate.now().minusWeeks(5)

        catVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository).saveAll(
            argThat { vaccinations: List<Vaccination> ->
                vaccinations.any { vaccination ->
                    vaccination.name == "Deworming" &&
                        vaccination.status == VaccinationStatus.PENDING &&
                        vaccination.pet == pet
                }
            },
        )
    }
}
