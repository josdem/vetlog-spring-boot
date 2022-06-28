/*
Copyright 2022 José Luis De la Cruz Morales joseluis.delacruz@gmail.com
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

package com.jos.dem.vetlog.command;

import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.enums.PetType;
import com.jos.dem.vetlog.model.PetImage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class PetCommand implements Command {

    private Long id;

    @NotNull
    @Size(min=1, max=50)
    private String name;

    @NotNull
    @Size(min=10, max=10)
    private String birthDate;

    @NotNull
    private Boolean dewormed = false;

    @NotNull
    private Boolean sterilized = false;

    @NotNull
    private Boolean vaccinated = false;

    @NotNull
    @Min(1L)
    private Long breed;

    @Min(1L)
    private Long user;

    @Min(1L)
    private Long adopter;

    @NotNull
    private PetType type;

    private String uuid;

    private PetStatus status;

    private MultipartFile image;

    private List<PetImage> images;
}
