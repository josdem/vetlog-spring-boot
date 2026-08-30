/*
  Copyright 2026 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.util

import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

internal class PetSplitterTest {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Test
    fun `should return empty list when no pets are provided`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pets = PetSplitter.split("")
        assertTrue(pets.isEmpty(), "Expected an empty list when no pets are provided")
    }

    @Test
    fun `should return empty list when null is provided`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pets = PetSplitter.split(null)
        assertTrue(pets.isEmpty(), "Expected an empty list when null is provided")
    }

    @Test
    fun `should return a single pet when one is provided`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pets = PetSplitter.split("338")
        assertEquals(1, pets.size, "Expected a list with one pet")
        assertIterableEquals(listOf(338L), pets, "Expected the pet ID to be 338")
    }

    @Test
    fun `should return multiple pets when a comma-separated list is provided`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pets = PetSplitter.split("338, 339, 340")
        assertEquals(3, pets.size, "Expected a list with three pets")
        assertIterableEquals(listOf(338L, 339L, 340L), pets, "Expected the pet IDs to be 338, 339, and 340")
    }

    @Test
    fun `should ignore string values in the list`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pets = PetSplitter.split("338, 339, invalid, 340")
        assertEquals(3, pets.size, "Expected a list with three valid pets")
        assertIterableEquals(listOf(338L, 339L, 340L), pets, "Expected the pet IDs to be 338, 339, and 340")
    }
}
