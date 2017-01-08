package com.jos.dem.vetlog.command

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import org.hibernate.validator.constraints.Email

class UserCommand implements Command {

  @NotNull
  @Size(min=6, max=50)
  String username

  @NotNull
  @Size(min=8, max=50)
  String password

  @NotNull
  @Size(min=8, max=50)
  String passwordConfirmation

  @NotNull
  @Size(min=1, max=50)
  String name

  @NotNull
  @Size(min=1, max=100)
  String lastname

  @Email
  String email

}
