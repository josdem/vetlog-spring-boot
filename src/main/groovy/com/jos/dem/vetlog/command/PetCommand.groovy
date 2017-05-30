/*
Copyright 2017 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.command

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import javax.validation.constraints.Min
import javax.validation.constraints.Max

import org.springframework.web.multipart.MultipartFile

import com.jos.dem.vetlog.model.PetType
import com.jos.dem.vetlog.model.PetImage
import com.jos.dem.vetlog.enums.PetStatus

class PetCommand implements Command {

  Long id

  @NotNull
  @Size(min=1, max=50)
  String name

  @NotNull
  String birthDate

  @NotNull
  Boolean dewormed = false

  @NotNull
  Boolean sterilized = false

  @NotNull
  Boolean vaccinated = false

  @NotNull
  @Min(1L)
  Long breed

  @Min(1L)
  Long user

  @Min(1L)
  Long adopter

  @NotNull
  PetType type

  String uuid

  PetStatus status

  MultipartFile image

  List<PetImage> images = []

}
