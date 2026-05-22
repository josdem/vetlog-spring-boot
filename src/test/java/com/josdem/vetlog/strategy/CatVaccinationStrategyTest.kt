package com.josdem.vetlog.strategy

import com.josdem.vetlog.enums.VaccinationStatus
import com.josdem.vetlog.model.Pet
import com.josdem.vetlog.model.Vaccination
import com.josdem.vetlog.repository.VaccinationRepository
import com.josdem.vetlog.strategy.vaccination.impl.CatVaccinationStrategy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
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
    @CsvSource("9, 2", "12, 2", "13, 2", "16, 2")
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

    @ParameterizedTest
    @CsvSource("9", "12", "16")
    fun `should save second vaccines for cat between 9 to 16 weeks`(weeks: Int) {
        log.info("Running test: should save second vaccines for cat between 9 to 16 weeks")
        pet.birthDate = LocalDate.now().minusWeeks(weeks.toLong())

        catVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository).saveAll(
            argThat { vaccinations: List<Vaccination> ->
                vaccinations.size == 2 &&
                    vaccinations.any { it.name == "TRICAT" } &&
                    vaccinations.any { it.name == "Deworming" }
            },
        )
    }

    @Test
    fun `should save annual vaccines for cat older than 16 weeks`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        pet.birthDate = LocalDate.now().minusWeeks(23)

        catVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository).saveAll(
            argThat { vaccinations: List<Vaccination> ->
                vaccinations.size == 2 &&
                    vaccinations.any { it.name == "TRICAT" } &&
                    vaccinations.any { it.name == "Deworming" }
            },
        )
    }

    @ParameterizedTest
    @CsvSource(
        "5, Deworming",
        "10, TRICAT",
        "20, TRICAT",
    )
    fun `should apply correct vaccine based on cat age`(
        weeks: Int,
        expectedVaccine: String,
    ) {
        log.info("Running test with age=$weeks weeks, expecting vaccine=$expectedVaccine")
        pet.birthDate = LocalDate.now().minusWeeks(weeks.toLong())

        catVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository).saveAll(
            argThat { vaccinations: List<Vaccination> ->
                val isSecondVaccine = weeks in 9..16
                val isAnnualSchedule = weeks > 16
                if (isSecondVaccine || isAnnualSchedule) {
                    vaccinations.size == 2 &&
                        vaccinations.any { it.name == "TRICAT" } &&
                        vaccinations.any { it.name == "Deworming" }
                } else {
                    vaccinations.any { vaccination ->
                        vaccination.name == expectedVaccine &&
                            vaccination.status == VaccinationStatus.PENDING &&
                            vaccination.pet == pet
                    }
                }
            },
        )
    }
}
