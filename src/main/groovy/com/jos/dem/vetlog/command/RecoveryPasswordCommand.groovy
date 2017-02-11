package com.jos.dem.vetlog.command

import org.hibernate.validator.constraints.Email

class RecoveryPasswordCommand implements Command {

  @Email
  String email

}
