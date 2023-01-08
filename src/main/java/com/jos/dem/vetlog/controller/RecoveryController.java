/*
Copyright 2023 Jose Morales joseluis.delacruz@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.jos.dem.vetlog.controller;

import com.jos.dem.vetlog.command.ChangePasswordCommand;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.RecoveryPasswordCommand;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.RecoveryService;
import com.jos.dem.vetlog.validator.ChangePasswordValidator;
import com.jos.dem.vetlog.validator.RecoveryPasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/recovery")
@RequiredArgsConstructor
public class RecoveryController {

  private final RecoveryService recoveryService;
  private final RecoveryPasswordValidator recoveryPasswordValidator;
  private final ChangePasswordValidator changePasswordValidator;
  private final LocaleService localeService;

  @InitBinder("recoveryPassword")
  private void initPasswordBinder(WebDataBinder binder) {
    binder.addValidators(recoveryPasswordValidator);
  }

  @InitBinder("changePassword")
  private void initChangeBinder(WebDataBinder binder) {
    binder.addValidators(changePasswordValidator);
  }

  @GetMapping(value = "/activate/{token}")
  public String create(@PathVariable String token) {
    log.info("Calling activate token");
    recoveryService.confirmAccountForToken(token);
    return "login/login";
  }

  @PostMapping(value = "/password")
  public ModelAndView generateTokenToChangePassword(
      @Valid RecoveryPasswordCommand command,
      BindingResult bindingResult,
      HttpServletRequest request) {
    log.info("Calling generate token for changing password");
    if (bindingResult.hasErrors()) {
      return new ModelAndView("recovery/recoveryPassword");
    }
    ModelAndView modelAndView = new ModelAndView("login/login");
    modelAndView.addObject("message", localeService.getMessage("recovery.email.sent", request));
    recoveryService.generateRegistrationCodeForEmail(command.getEmail());
    return modelAndView;
  }

  @GetMapping(value = "/password")
  public ModelAndView recoveryPassword() {
    log.info("Calling recovery password");
    ModelAndView modelAndView = new ModelAndView("recovery/recoveryPassword");
    Command recoveryPasswordCommand = new RecoveryPasswordCommand();
    modelAndView.addObject("recoveryPasswordCommand", recoveryPasswordCommand);
    return modelAndView;
  }

  @GetMapping(value = "/forgot/{token}")
  public ModelAndView changePassword(@PathVariable String token, HttpServletRequest request) {
    log.info("Calling change password");
    ModelAndView modelAndView = new ModelAndView("recovery/changePassword");
    Boolean valid = recoveryService.validateToken(token);
    if (!valid) {
      modelAndView.addObject("message", localeService.getMessage("recovery.token.error", request));
    }
    ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand();
    changePasswordCommand.setToken(token);
    modelAndView.addObject("changePasswordCommand", changePasswordCommand);
    return modelAndView;
  }

  @PostMapping(value = "/change")
  public ModelAndView generateTokenToChangePassword(
      @Valid ChangePasswordCommand command,
      BindingResult bindingResult,
      HttpServletRequest request) {
    log.info("Calling save and changing password");
    if (bindingResult.hasErrors()) {
      return new ModelAndView("recovery/changePassword");
    }
    ModelAndView modelAndView = new ModelAndView("login/login");
    modelAndView.addObject(
        "message", localeService.getMessage("recovery.password.changed", request));
    recoveryService.changePassword(command);
    return modelAndView;
  }
}
