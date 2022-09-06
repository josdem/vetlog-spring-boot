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

import com.jos.dem.vetlog.command.AdoptionCommand;
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.service.AdoptionService;
import com.jos.dem.vetlog.service.PetService;
import com.jos.dem.vetlog.validator.AdoptionValidator;
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

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/adoption")
public class AdoptionController {

  @Autowired private PetService petService;
  @Autowired private AdoptionValidator adoptionValidator;
  @Autowired private AdoptionService adoptionService;

  @Value("${gcpImageUrl}")
  private String gcpImageUrl;

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @InitBinder("adoptionCommand")
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(adoptionValidator);
  }

  @RequestMapping(method = GET, value = "/descriptionForAdoption")
  public ModelAndView descriptionForAdoption(AdoptionCommand adoptionCommand) {
    log.info("Adding description to pet with uuid: " + adoptionCommand.getUuid());
    ModelAndView modelAndView = new ModelAndView();
    return fillPetAndAdoptionCommand(modelAndView, adoptionCommand);
  }

  @RequestMapping(method = POST, value = "/save")
  public ModelAndView save(@Valid AdoptionCommand adoptionCommand, BindingResult bindingResult) {
    log.info("Creating adoption description for pet: " + adoptionCommand.getUuid());
    if (bindingResult.hasErrors()) {
      ModelAndView modelAndView = new ModelAndView("adoption/descriptionForAdoption");
      fillPetAndAdoptionCommand(modelAndView, adoptionCommand);
      return modelAndView;
    }
    adoptionService.save(adoptionCommand);
    List<Pet> pets = petService.getPetsByStatus(PetStatus.IN_ADOPTION);
    ModelAndView modelAndView = new ModelAndView("pet/listForAdoption");
    modelAndView.addObject("pets", pets);
    modelAndView.addObject("gcpImageUrl", gcpImageUrl);
    return modelAndView;
  }

  private ModelAndView fillPetAndAdoptionCommand(
      ModelAndView modelAndView, AdoptionCommand adoptionCommand) {
    Pet pet = petService.getPetByUuid(adoptionCommand.getUuid());
    modelAndView.addObject("pet", pet);
    modelAndView.addObject("adoptionCommand", adoptionCommand);
    modelAndView.addObject("gcpImageUrl", gcpImageUrl);
    return modelAndView;
  }
}
