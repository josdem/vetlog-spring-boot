package com.jos.dem.vetlog.command

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import javax.validation.constraints.Min
import javax.validation.constraints.Max

import com.jos.dem.vetlog.model.PetType

class PetCommand implements Command {

  @NotNull
  @Size(min=1, max=50)
  String name

  @NotNull
  Date birthDate

  @NotNull
  Boolean dewormed = true

  @NotNull
  Boolean sterilized = true

  @NotNull
  Boolean vaccinated = true

  @NotNull
  @Min(1L)
  Long breed

  @NotNull
  PetType type

}
