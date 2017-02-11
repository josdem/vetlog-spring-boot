package com.jos.dem.vetlog.validator

import org.springframework.validation.Validator
import org.springframework.validation.Errors
import org.springframework.stereotype.Component

import com.jos.dem.vetlog.command.RecoveryPasswordCommand

@Component
class RecoveryPasswordValidator implements Validator {

  @Override
  boolean supports(Class<?> clazz) {
    RecoveryPasswordCommand.class.equals(clazz)
  }

  @Override
  void validate(Object target, Errors errors) {
    RecoveryPasswordCommand command = (RecoveryPasswordCommand) target
  }

}
