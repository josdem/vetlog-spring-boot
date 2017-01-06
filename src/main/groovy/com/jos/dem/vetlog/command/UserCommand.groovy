package com.jos.dem.vetlog.command

import org.hibernate.validator.constraints.NotEmpty
import org.hibernate.validator.constraints.Email

class UserCommand implements Command {

  @NotEmpty(message = "Username can not be empty")	
  String username

  @NotEmpty(message = "Password can not be empty")
  String password

  @NotEmpty(message = "Confirm password can not be empty")
  String passwordConfirmation

  @NotEmpty(message = "Name can not be empty")
  String name

  @NotEmpty(message = "Lastname can not be empty")
  String lastname

  @Email
  String email
  
}