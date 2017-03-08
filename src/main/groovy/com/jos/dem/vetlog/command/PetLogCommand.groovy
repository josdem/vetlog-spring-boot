package com.jos.dem.vetlog.command

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import javax.validation.constraints.Min
import javax.validation.constraints.Max

class PetLogCommand implements Command {

  @Size(max=100)
  String vetName

  @NotNull
  @Size(min=1, max=250)
  String symptoms

  @NotNull
  @Size(min=1, max=250)
  String diagnosis

  @Size(min=1, max=250)
  String medicine

}
