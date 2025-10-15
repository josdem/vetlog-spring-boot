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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.enums.WeightUnits;
import com.josdem.vetlog.model.PetImage;
import com.josdem.vetlog.model.Vaccination;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record PetRecord(
        Long id,
        @Size(min = 1, max = 50) String name,
        @NotNull String birthDate,
        Boolean sterilized,
        String chip_id,
        @Min(1L) Long breed,
        @NotNull(message = "Weight should not be empty") @DecimalMin(value = "0.0") @DecimalMax(value = "100.0")
                BigDecimal weight,
        @NotNull WeightUnits unit,
        @Min(1L) Long user,
        @Min(1L) Long adopter,
        @NotNull PetType type,
        String uuid,
        PetStatus status,
        @JsonIgnore MultipartFile image,
        @JsonIgnore List<PetImage> images,
        @JsonIgnore List<Vaccination> vaccines)
        implements IRecord {

    public static final String DEFAULT_CHIP_ID = "800000000000001";

    // Canonical constructor ensures non-null collections and defaults
    public PetRecord {
        sterilized = sterilized != null ? sterilized : false;
        chip_id = chip_id != null ? chip_id : DEFAULT_CHIP_ID;
        images = images != null ? images : new java.util.ArrayList<>();
        vaccines = vaccines != null ? vaccines : new java.util.ArrayList<>();
    }

    // ✅ Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String birthDate;
        private Boolean sterilized;
        private String chip_id;
        private Long breed;
        private BigDecimal weight;
        private WeightUnits unit;
        private Long user;
        private Long adopter;
        private PetType type;
        private String uuid;
        private PetStatus status;
        private MultipartFile image;
        private List<PetImage> images;
        private List<Vaccination> vaccines;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder birthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder sterilized(Boolean sterilized) {
            this.sterilized = sterilized;
            return this;
        }

        public Builder chipId(String chip_id) {
            this.chip_id = chip_id;
            return this;
        }

        public Builder breed(Long breed) {
            this.breed = breed;
            return this;
        }

        public Builder weight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }

        public Builder unit(WeightUnits unit) {
            this.unit = unit;
            return this;
        }

        public Builder user(Long user) {
            this.user = user;
            return this;
        }

        public Builder adopter(Long adopter) {
            this.adopter = adopter;
            return this;
        }

        public Builder type(PetType type) {
            this.type = type;
            return this;
        }

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder status(PetStatus status) {
            this.status = status;
            return this;
        }

        public Builder image(MultipartFile image) {
            this.image = image;
            return this;
        }

        public Builder images(List<PetImage> images) {
            this.images = images;
            return this;
        }

        public Builder vaccines(List<Vaccination> vaccines) {
            this.vaccines = vaccines;
            return this;
        }

        public PetRecord build() {
            // Validate required fields manually if necessary
            // Objects.requireNonNull(name, "Name must not be null");
            // Objects.requireNonNull(birthDate, "Birth date must not be null");
            // Objects.requireNonNull(weight, "Weight must not be null");
            // Objects.requireNonNull(unit, "Unit must not be null");
            //  Objects.requireNonNull(type, "Type must not be null");

            return new PetRecord(
                    id,
                    name,
                    birthDate,
                    sterilized,
                    chip_id,
                    breed,
                    weight,
                    unit,
                    user,
                    adopter,
                    type,
                    uuid,
                    status,
                    image,
                    images,
                    vaccines);
        }
    }
    // Similarly, you can add withXxx() methods for other fields if needed

}
