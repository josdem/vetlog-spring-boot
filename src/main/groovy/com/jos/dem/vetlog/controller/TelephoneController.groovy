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

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller

import javax.validation.Valid
import javax.servlet.http.HttpServletRequest

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.TelephoneCommand
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.service.TelephoneService
import com.jos.dem.vetlog.validator.TelephoneValidator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/telephone")
class TelephoneController {

  @Autowired
  PetService petService
  @Autowired
  UserService userService
  @Autowired
  LocaleService localeService
  @Autowired
  TelephoneService telephoneService
  @Autowired
  TelephoneValidator telephoneValidator

  @Value('${awsImageUrl}')
  String awsImageUrl

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder('telephoneCommand')
	private void initBinder(WebDataBinder binder) {
		binder.addValidators(telephoneValidator)
	}

  @RequestMapping(method = POST, value = "/save")
  ModelAndView save(@Valid TelephoneCommand telephoneCommand, BindingResult bindingResult, HttpServletRequest request) {
    log.info "Saving adoption for pet: ${telephoneCommand.uuid}"
    if (bindingResult.hasErrors()) {
      ModelAndView modelAndView = new ModelAndView('telephone/adopt')
      fillPetAndTelephoneCommand(modelAndView, telephoneCommand)
      return modelAndView
    }
    User user = userService.getCurrentUser()
    telephoneService.save(telephoneCommand, user)
    ModelAndView modelAndView = new ModelAndView('redirect:/')
    modelAndView.addObject('message', localeService.getMessage('adoption.email.sent', request))
    modelAndView
  }

  @RequestMapping(method = GET, value = "/adopt")
  ModelAndView adopt(TelephoneCommand telephoneCommand){
    log.info "Adoption to pet with uuid: ${telephoneCommand.uuid}"
    ModelAndView modelAndView = new ModelAndView('telephone/adopt')
    fillPetAndTelephoneCommand(modelAndView, telephoneCommand)
  }

  private fillPetAndTelephoneCommand(ModelAndView modelAndView, TelephoneCommand telephoneCommand){
    Pet pet = petService.getPetByUuid(telephoneCommand.uuid)
    modelAndView.addObject('pet', pet)
    modelAndView.addObject('telephoneCommand', telephoneCommand)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

}
