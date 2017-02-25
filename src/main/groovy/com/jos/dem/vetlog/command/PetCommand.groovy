package com.jos.dem.vetlog.command

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import javax.validation.constraints.Min
import javax.validation.constraints.Max

class PetCommand implements Command {

  @NotNull
  @Size(min=1, max=50)
  String name

  @NotNull
  @Min(0L)
  @Max(250L)
  Integer age

  @NotNull
  Boolean dewormed = true

  @NotNull
  Boolean sterilized = true

  @NotNull
  Boolean vaccinated = true

  @NotNull
  @Min(1L)
  Long breed

}
