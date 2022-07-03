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

import com.jos.dem.vetlog.command.Command

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller

import javax.validation.Valid
import javax.servlet.http.HttpServletRequest


import com.jos.dem.vetlog.command.UserCommand
import com.jos.dem.vetlog.validator.UserValidator
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.LocaleService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/user")
class UserController {

  @Autowired
  UserValidator userValidator
  @Autowired
  UserService userService
  @Autowired
  LocaleService localeService

  Logger log = LoggerFactory.getLogger(this.class)

	@InitBinder('userCommand')
	private void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidator)
	}

	@RequestMapping(method = GET, value = "/create")
	ModelAndView create(){
		Command userCommand = new UserCommand()
    fillUserCommand(userCommand)
	}

	@RequestMapping(method = POST, value = "/save")
	ModelAndView save(@Valid UserCommand userCommand, BindingResult bindingResult, HttpServletRequest request) {
    log.info "Saving user: ${userCommand.username}"
    if (bindingResult.hasErrors()) {
      return fillUserCommand(userCommand)
    }
    userService.save(userCommand)
    ModelAndView modelAndView = new ModelAndView('redirect:/')
    modelAndView.addObject('message', localeService.getMessage('user.account.created', request))
    modelAndView
  }

  private ModelAndView fillUserCommand(Command userCommand){
		ModelAndView modelAndView = new ModelAndView('user/create')
    modelAndView.addObject('userCommand', userCommand)
		modelAndView
  }

}
