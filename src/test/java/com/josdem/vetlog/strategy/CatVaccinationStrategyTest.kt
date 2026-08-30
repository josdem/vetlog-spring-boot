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
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
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
    @CsvSource("0, 1", "3, 1", "4, 1", "8, 1", "9, 2", "12, 2", "13, 2", "16, 2", "17, 2", "20, 2", "21, 2", "24, 2")
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
    @CsvSource("0", "3", "8")
    fun `should save first vaccines for cat between 0 to 8 weeks`(weeks: Int) {
        log.info("Running test: should save first vaccines for cat between 0 to 8 weeks")
        pet.birthDate = LocalDate.now().minusWeeks(weeks.toLong())

        catVaccinationStrategy.vaccinate(pet)

        verify(vaccinationRepository).saveAll(
            argThat { vaccinations: List<Vaccination> ->
                vaccinations.size == 1 &&
                    vaccinations.any { it.name == "Deworming" }
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
        "10, Deworming",
        "20, TRICAT",
        "20, Deworming",
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
                vaccinations.any { vaccination ->
                    vaccination.name == expectedVaccine &&
                        vaccination.status == VaccinationStatus.PENDING &&
                        vaccination.pet == pet
                }
            },
        )
    }
}
