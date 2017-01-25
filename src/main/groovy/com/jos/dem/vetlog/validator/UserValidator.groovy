package com.jos.dem.vetlog.validator

import org.springframework.validation.Validator
import org.springframework.validation.Errors
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.command.UserCommand

@Component
class UserValidator implements Validator {

  @Autowired
  LocaleService localeService
  @Autowired
  UserService userService

  @Override
  boolean supports(Class<?> clazz) {
    UserCommand.class.equals(clazz)
  }

  @Override
  void validate(Object target, Errors errors) {
    UserCommand userCommand = (UserCommand) target
    validatePasswords(errors, userCommand)
    validateUsername(errors, userCommand)
    validateEmail(errors, userCommand)
  }

  def validatePasswords(Errors errors, UserCommand command) {
    if (!command.password.equals(command.passwordConfirmation)){
      errors.reject('password', localeService.getMessage('user.validation.password.equals'))
    }
  }

  def validateUsername(Errors errors, UserCommand command) {
    if (userService.getByUsername(command.username)){
       errors.reject("username", localeService.getMessage('user.validation.duplicated.username'))
    }
  }

  def validateEmail(Errors errors, UserCommand command) {
    if (userService.getByEmail(command.email)){
       errors.reject("email", localeService.getMessage('user.validation.duplicated.email'))
    }
  }

}
