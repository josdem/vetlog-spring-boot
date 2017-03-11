package com.jos.dem.vetlog.command

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import javax.validation.constraints.Min
import javax.validation.constraints.Max

class PetLogCommand implements Command {

  @Size(max=200)
  String vetName

  @NotNull
  @Size(min=1, max=1000)
  String symptoms

  @NotNull
  @Size(min=1, max=1000)
  String diagnosis

  @Size(min=1, max=500)
  String medicine

  @NotNull
  @Min(1L)
  Long pet

}
