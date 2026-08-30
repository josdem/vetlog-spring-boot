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

package com.josdem.vetlog.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PetSplitter {

    private static final String NUMBER = "\\d+";

    public static List<Long> split(String pets) {
        if (pets == null || pets.isEmpty()) {
            return Collections.emptyList();
        }
        var petIds = pets.split(",");
        return Arrays.stream(petIds)
                .map(String::trim)
                .filter(petId -> !petId.isEmpty())
                .filter(petId -> petId.matches(NUMBER))
                .map(Long::parseLong)
                .toList();
    }
}
