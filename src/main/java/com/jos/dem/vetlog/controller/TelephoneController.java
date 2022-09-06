/*
Copyright 2022 Jose Morales joseluis.delacruz@gmail.com

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

import com.jos.dem.vetlog.command.TelephoneCommand;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.PetService;
import com.jos.dem.vetlog.service.TelephoneService;
import com.jos.dem.vetlog.service.UserService;
import com.jos.dem.vetlog.validator.TelephoneValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/telephone")
public class TelephoneController {

  @Autowired private PetService petService;
  @Autowired private UserService userService;
  @Autowired private LocaleService localeService;
  @Autowired private TelephoneService telephoneService;
  @Autowired private TelephoneValidator telephoneValidator;

  @Value("${gcpImageUrl}")
  private String gcpImageUrl;

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @InitBinder("telephoneCommand")
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(telephoneValidator);
  }

  @RequestMapping(method = POST, value = "/save")
  public ModelAndView save(
      @Valid TelephoneCommand telephoneCommand,
      BindingResult bindingResult,
      HttpServletRequest request) {
    log.info("Saving adoption for pet: " + telephoneCommand.getUuid());
    if (bindingResult.hasErrors()) {
      ModelAndView modelAndView = new ModelAndView("telephone/adopt");
      return fillPetAndTelephoneCommand(modelAndView, telephoneCommand);
    }
    User user = userService.getCurrentUser();
    telephoneService.save(telephoneCommand, user);
    ModelAndView modelAndView = new ModelAndView("redirect:/");
    modelAndView.addObject("message", localeService.getMessage("adoption.email.sent", request));
    return modelAndView;
  }

  @RequestMapping(method = GET, value = "/adopt")
  public ModelAndView adopt(TelephoneCommand telephoneCommand) {
    log.info("Adoption to pet with uuid: " + telephoneCommand.getUuid());
    ModelAndView modelAndView = new ModelAndView("telephone/adopt");
    return fillPetAndTelephoneCommand(modelAndView, telephoneCommand);
  }

  private ModelAndView fillPetAndTelephoneCommand(
      ModelAndView modelAndView, TelephoneCommand telephoneCommand) {
    Pet pet = petService.getPetByUuid(telephoneCommand.getUuid());
    modelAndView.addObject("pet", pet);
    modelAndView.addObject("telephoneCommand", telephoneCommand);
    modelAndView.addObject("gcpImageUrl", gcpImageUrl);
    return modelAndView;
  }
}
