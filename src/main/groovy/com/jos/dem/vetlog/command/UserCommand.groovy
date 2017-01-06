package com.jos.dem.vetlog.command

import org.hibernate.validator.constraints.NotEmpty
import org.hibernate.validator.constraints.Email

class UserCommand implements Command {

  @NotEmpty("Username can not be empty")
  String username

  @NotEmpty("Password can not be empty")
  String password

  @NotEmpty("Password confirmation can not be empty")
  String passwordConfirmation

  @NotEmpty("Name can not be empty")
  String name

  @NotEmpty("Lastname can not be empty")
  String lastname

  @Email
  String email
}