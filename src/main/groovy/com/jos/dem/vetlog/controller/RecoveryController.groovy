/*
Copyright 2017 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.controller

import com.jos.dem.vetlog.command.ChangePasswordCommand
import com.jos.dem.vetlog.command.RecoveryPasswordCommand

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller

import javax.validation.Valid
import javax.servlet.http.HttpServletRequest

import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.service.LocaleService

import com.jos.dem.vetlog.validator.RecoveryPasswordValidator
import com.jos.dem.vetlog.validator.ChangePasswordValidator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/recovery")
class RecoveryController {

  @Autowired
  RecoveryService recoveryService
  @Autowired
  RecoveryPasswordValidator recoveryPasswordValidator
  @Autowired
  ChangePasswordValidator changePasswordValidator
  @Autowired
  LocaleService localeService

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder('recoveryPassword')
	private void initPasswordBinder(WebDataBinder binder) {
		binder.addValidators(recoveryPasswordValidator)
	}

  @InitBinder('changePassword')
	private void initChangeBinder(WebDataBinder binder) {
		binder.addValidators(changePasswordValidator)
	}

	@RequestMapping(method = GET, value = "/activate/{token}")
	String create(@PathVariable String token){
  	log.info "Calling activate token"
    recoveryService.confirmAccountForToken(token)
    'login/login'
	}

  @RequestMapping(method = POST, value = "/password")
  ModelAndView generateTokenToChangePassword(@Valid RecoveryPasswordCommand command, BindingResult bindingResult, HttpServletRequest request){
  	log.info "Calling generate token for changing password"
    if(bindingResult.hasErrors()){
      return new ModelAndView('recovery/recoveryPassword')
    }
    ModelAndView modelAndView = new ModelAndView('login/login')
    modelAndView.addObject('message', localeService.getMessage('recovery.email.sent', request))
    recoveryService.generateRegistrationCodeForEmail(command.email)
    modelAndView
  }

	@RequestMapping(method = GET, value = "/password")
  ModelAndView recoveryPassword(){
  	log.info "Calling recovery password"
    def modelAndView = new ModelAndView('recovery/recoveryPassword')
		def recoveryPasswordCommand = new RecoveryPasswordCommand()
		modelAndView.addObject('recoveryPasswordCommand', recoveryPasswordCommand)
		modelAndView
  }

  @RequestMapping(method = GET, value = "/forgot/{token}")
    ModelAndView changePassword(@PathVariable String token, HttpServletRequest request){
    log.info "Calling change password"
    def modelAndView = new ModelAndView('recovery/changePassword')
    Boolean valid = recoveryService.validateToken(token)
    if(!valid){
      modelAndView.addObject('message', localeService.getMessage('recovery.token.error', request))
    }
    def changePasswordCommand = new ChangePasswordCommand(token:token)
    modelAndView.addObject('changePasswordCommand', changePasswordCommand)
    modelAndView
  }

  @RequestMapping(method = POST, value = "/change")
  ModelAndView generateTokenToChangePassword(@Valid ChangePasswordCommand command, BindingResult bindingResult, HttpServletRequest request){
  	log.info "Calling save and changing password"
    if(bindingResult.hasErrors()){
      return new ModelAndView('recovery/changePassword')
    }
    ModelAndView modelAndView = new ModelAndView('login/login')
    modelAndView.addObject('message', localeService.getMessage('recovery.password.changed', request))
    recoveryService.changePassword(command)
    modelAndView
  }

}
