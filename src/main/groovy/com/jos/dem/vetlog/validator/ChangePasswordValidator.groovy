package com.jos.dem.vetlog.validator

import org.springframework.validation.Validator
import org.springframework.validation.Errors
import org.springframework.stereotype.Component

import com.jos.dem.vetlog.command.ChangePasswordCommand

@Component
class ChangePasswordValidator implements Validator {

  @Override
  boolean supports(Class<?> clazz) {
    ChangePasswordCommand.class.equals(clazz)
  }

  @Override
  void validate(Object target, Errors errors) {
    ChangePasswordCommand command = (ChangePasswordCommand) target
    validatePasswords(errors, ChangePasswordCommand)
  }

  def validatePasswords(Errors errors, ChangePasswordCommand command) {
    if (!command.password.equals(command.passwordConfirmation)){
      errors.reject('password', localeService.getMessage('user.validation.password.equals'))
    }
  }

}
