package com.jos.dem.vetlog.command

import javax.validation.constraints.Size
import javax.validation.constraints.NotNull
import org.hibernate.validator.constraints.Email

class ChangePasswordCommand implements Command {

  @NotNull
  @Size(min=32, max=32)
  String token

  @NotNull
  @Size(min=8, max=50)
  String password

  @NotNull
  @Size(min=8, max=50)
  String passwordConfirmation

}
