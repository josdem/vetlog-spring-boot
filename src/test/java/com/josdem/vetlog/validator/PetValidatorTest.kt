package com.josdem.vetlog.validator

import com.josdem.vetlog.command.PetCommand
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.slf4j.LoggerFactory
import org.springframework.validation.Errors
import java.time.LocalDate

internal class PetValidatorTest {
    companion object {
        private val log = LoggerFactory.getLogger(PetValidatorTest::class.java)
    }

    private val validator = PetValidator()
    private val errors: Errors = mock()

    @Test
    fun `should validate birthdate`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val petCommand = getPetCommand("2021-01-17")
        validator.validate(petCommand, errors)
        verify(errors, never()).rejectValue(any(), any())
    }

    @Test
    fun `should validate empty birthdate`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val petCommand = getPetCommand("")
        validator.validate(petCommand, errors)
        verify(errors, never()).rejectValue(any(), any())
    }

    @Test
    fun `should reject a birthdate`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val futureDate = LocalDate.now().plusMonths(1).toString()
        val petCommand = getPetCommand(futureDate)
        validator.validate(petCommand, errors)
        verify(errors).rejectValue("birthDate", "pet.error.birthDate.past")
    }

    private fun getPetCommand(birthdate: String): PetCommand = PetCommand().apply { birthDate = birthdate }
}
