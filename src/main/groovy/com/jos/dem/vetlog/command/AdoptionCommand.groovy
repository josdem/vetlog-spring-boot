package com.jos.dem.vetlog.command

import javax.validation.constraints.Size
import javax.validation.constraints.NotNull

class AdoptionCommand implements Command {

  @NotNull
  String uuid

  @NotNull
  @Size(min=1, max=1000)
  String description

}
