package com.jos.dem.vetlog.command

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import javax.validation.constraints.Min
import javax.validation.constraints.Max
import javax.validation.constraints.Past

import org.springframework.web.multipart.MultipartFile

import com.jos.dem.vetlog.model.PetType
import com.jos.dem.vetlog.model.PetImage

class PetCommand implements Command {

  @NotNull
  @Size(min=1, max=50)
  String name

  @NotNull
  @Past
  Date birthDate

  @NotNull
  Boolean dewormed = false

  @NotNull
  Boolean sterilized = false

  @NotNull
  Boolean vaccinated = false

  @NotNull
  @Min(1L)
  Long breed

  @NotNull
  PetType type

  MultipartFile image

  List<PetImage> images = []

}
