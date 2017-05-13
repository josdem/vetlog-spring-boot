package com.jos.dem.vetlog.command

import javax.validation.constraints.Size
import javax.validation.constraints.NotNull

class TelephoneCommand implements Command {

  @NotNull
  String uuid

  @NotNull
  @Size(min=1, max=10)
  String mobile

}
