/*
  Copyright 2025 Jose Morales contact@josdem.io

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

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory

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
        assertTrue(pets.size == 1 && pets[0] == 338L, "Expected a list with one pet")
    }

    @Test
    fun `should return multiple pets when a comma-separated list is provided`(testInfo: TestInfo) {
        log.info(testInfo.displayName)
        val pets = PetSplitter.split("338, 339, 340")
        assertTrue(pets.size == 3 && pets.containsAll(listOf(338L, 339L, 340L)), "Expected a list with three pets")
    }
}
