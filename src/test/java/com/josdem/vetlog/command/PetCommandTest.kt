package com.josdem.vetlog.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PetCommandTest {
    @Test
    fun `should validate initial weight`() {
        val command =
            PetCommand().apply {
                id = 1L
            }

        assertEquals(BigDecimal("0.00"), command.weight)
    }
}
