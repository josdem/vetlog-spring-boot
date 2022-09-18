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

import com.jos.dem.vetlog.binder.PetBinder;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetCommand;
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.enums.PetType;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.service.BreedService;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.PetService;
import com.jos.dem.vetlog.service.UserService;
import com.jos.dem.vetlog.util.DateFormatter;
import com.jos.dem.vetlog.validator.PetValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/pet")
public class PetController {

  @Autowired private BreedService breedService;
  @Autowired private PetService petService;
  @Autowired private UserService userService;
  @Autowired private LocaleService localeService;
  @Autowired private PetBinder petBinder;
  @Autowired private PetValidator petValidator;
  @Autowired private DateFormatter dateFormatter;

  @Value("${breedsByTypeUrl}")
  private String breedsByTypeUrl;

  @Value("${gcpImageUrl}")
  private String gcpImageUrl;

  @Value("${defaultImage}")
  private String defaultImage;

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @InitBinder("petCommand")
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(petValidator);
  }

  @RequestMapping(method = GET, value = "/create")
  public ModelAndView create(@RequestParam(value = "type", required = false) String type) {
    ModelAndView modelAndView = new ModelAndView("pet/create");
    Command petCommand = new PetCommand();
    modelAndView.addObject("petCommand", petCommand);
    return fillModelAndView(modelAndView);
  }

  @RequestMapping(method = GET, value = "/edit")
  public ModelAndView edit(@RequestParam("uuid") String uuid) {
    log.info("Editing pet: " + uuid);
    Pet pet = petService.getPetByUuid(uuid);
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("petCommand", petBinder.bindPet(pet));
    modelAndView.addObject("breeds", breedService.getBreedsByType(PetType.DOG));
    modelAndView.addObject("breedsByTypeUrl", breedsByTypeUrl);
    modelAndView.addObject("awsImageUrl", gcpImageUrl);
    return modelAndView;
  }

  @RequestMapping(method = POST, value = "/update")
  public ModelAndView update(
      @Valid PetCommand petCommand, BindingResult bindingResult, HttpServletRequest request)
      throws IOException {
    log.info("Updating pet: " + petCommand.getName());
    ModelAndView modelAndView = new ModelAndView("pet/edit");
    if (bindingResult.hasErrors()) {
      modelAndView.addObject("petCommand", petCommand);
      return fillModelAndView(modelAndView);
    }
    petService.update(petCommand);
    modelAndView.addObject("message", localeService.getMessage("pet.updated", request));
    modelAndView.addObject("petCommand", petCommand);
    return fillModelAndView(modelAndView);
  }

  @RequestMapping(method = POST, value = "/save")
  public ModelAndView save(
      @Valid PetCommand petCommand, BindingResult bindingResult, HttpServletRequest request)
      throws IOException {
    log.info("Creating pet: " + petCommand.getName());
    ModelAndView modelAndView = new ModelAndView("pet/create");
    if (bindingResult.hasErrors()) {
      modelAndView.addObject("petCommand", petCommand);
      return fillModelAndView(modelAndView);
    }
    User user = userService.getCurrentUser();
    petService.save(petCommand, user);
    modelAndView.addObject("message", localeService.getMessage("pet.created", request));
    petCommand = new PetCommand();
    modelAndView.addObject("petCommand", petCommand);
    return fillModelAndView(modelAndView);
  }

  private ModelAndView fillModelAndView(ModelAndView modelAndView) {
    modelAndView.addObject("breeds", breedService.getBreedsByType(PetType.DOG));
    modelAndView.addObject("breedsByTypeUrl", breedsByTypeUrl);
    modelAndView.addObject("awsImageUrl", gcpImageUrl);
    return modelAndView;
  }

  @RequestMapping(method = GET, value = "/list")
  public ModelAndView list() {
    log.info("Listing pets");
    ModelAndView modelAndView = new ModelAndView();
    return fillPetAndImageUrl(modelAndView);
  }

  @RequestMapping(method = GET, value = "/giveForAdoption")
  public ModelAndView giveForAdoption() {
    log.info("Select a pet for adoption");
    ModelAndView modelAndView = new ModelAndView("pet/giveForAdoption");
    return fillPetAndImageUrl(modelAndView);
  }

  @RequestMapping(method = GET, value = "/listForAdoption")
  public ModelAndView listForAdoption(HttpServletRequest request) {
    log.info("Listing pets for adoption");
    ModelAndView modelAndView = new ModelAndView("pet/listForAdoption");
    List<Pet> pets = petService.getPetsByStatus(PetStatus.IN_ADOPTION);
    if (pets == null || pets.isEmpty()) {
      modelAndView.addObject("petListEmpty", localeService.getMessage("pet.list.empty", request));
    }
    modelAndView.addObject("pets", pets);
    modelAndView.addObject("gcpImageUrl", gcpImageUrl);
    return modelAndView;
  }

  private ModelAndView fillPetAndImageUrl(ModelAndView modelAndView) {
    User user = userService.getCurrentUser();
    List<Pet> pets = petService.getPetsByUser(user);
    modelAndView.addObject("pets", pets);
    modelAndView.addObject("gcpImageUrl", gcpImageUrl);
    modelAndView.addObject("defaultImage", defaultImage);
    return modelAndView;
  }
}
