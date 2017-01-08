package com.jos.dem.vetlog.validator

import org.springframework.validation.Validator
import org.springframework.validation.Errors
import org.springframework.stereotype.Component

import com.jos.dem.vetlog.command.UserCommand

@Component
class UserValidator implements Validator {

  @Override
  boolean supports(Class<?> clazz) {
    UserCommand.class.equals(clazz)
  }

  @Override
  void validate(Object target, Errors errors) {
    UserCommand UserCommand = (UserCommand) target
    validatePasswords(errors, UserCommand)
    validatePasswordConstraints(errors, UserCommand)
  }

  def validatePasswords(Errors errors, UserCommand command) {
    if (!command.password.equals(command.passwordConfirmation)){
      errors.reject('password', 'The passwords are not equals')
    }
  }

  def validatePasswordConstraints(Errors errors, UserCommand command) {
    def regex = ~/^[0-9a-zA-Z]{8,}$+/
    if(!command.password.matches(regex)){
      errors.reject('password', 'Password are bad formed')
    }
  }

}
