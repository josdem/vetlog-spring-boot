package com.jos.dem.vetlog.command

import javax.validation.constraints.NotNull

class AdoptionCommand implements Command {

  @NotNull
  String uuid

  @NotNull
  String description

}
