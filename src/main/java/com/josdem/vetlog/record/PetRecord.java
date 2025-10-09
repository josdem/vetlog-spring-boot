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

package com.josdem.vetlog.record;

import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.model.PetImage;
import com.josdem.vetlog.model.Vaccination;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record PetRecord(
        Long id,
        @Size(min = 1, max = 50) String name,
        @NotNull String birthDate,
        Boolean sterilized,
        @Min(1L) Long breed,
        @Min(1L) Long user,
        @Min(1L) Long adopter,
        @NotNull PetType type,
        String uuid,
        PetStatus status,

        // Transient / non-serialized fields
        MultipartFile image,
        List<PetImage> images,
        List<Vaccination> vaccines)
        implements IRecord {

    // Compact constructor to handle default values
    public PetRecord {
        if (sterilized == null) {
            sterilized = false;
        }
        if (images == null) {
            images = new ArrayList<>();
        }
        if (vaccines == null) {
            vaccines = new ArrayList<>();
        }
    }

    // Similarly, you can add withXxx() methods for other fields if needed
}
