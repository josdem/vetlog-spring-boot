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

import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetLogCommand;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.PetLog;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.PetLogService;
import com.jos.dem.vetlog.service.PetService;
import com.jos.dem.vetlog.service.UserService;
import com.jos.dem.vetlog.validator.PetLogValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@RequestMapping("/petlog")
@RequiredArgsConstructor
public class PetLogController {

  private final PetLogValidator petLogValidator;
  private final PetService petService;
  private final PetLogService petLogService;
  private final UserService userService;
  private final LocaleService localeService;

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(petLogValidator);
  }

  @RequestMapping(method = GET, value = "/create")
  public ModelAndView create(@RequestParam("uuid") String uuid, HttpServletRequest request) {
    log.info("Pet uuid: " + uuid);
    ModelAndView modelAndView = new ModelAndView("petlog/create");
    Command petLogCommand = new PetLogCommand();
    modelAndView.addObject("petLogCommand", petLogCommand);
    Pet pet = petService.getPetByUuid(uuid);
    User currentUser = userService.getCurrentUser();
    List<Pet> pets = getPetsFromUser(pet, currentUser);
    return fillModelAndView(modelAndView, pets, request);
  }

  @RequestMapping(method = POST, value = "/save")
  public ModelAndView save(
      @Valid PetLogCommand petLogCommand, BindingResult bindingResult, HttpServletRequest request) {
    log.info("Creating pet: " + petLogCommand.getPet());
    ModelAndView modelAndView = new ModelAndView("petlog/create");
    Pet pet = petService.getPetById(petLogCommand.getPet());
    User currentUser = userService.getCurrentUser();
    List<Pet> pets = getPetsFromUser(pet, currentUser);
    if (bindingResult.hasErrors()) {
      modelAndView.addObject("petLogCommand", petLogCommand);
      return fillModelAndView(modelAndView, pets, request);
    }
    petLogService.save(petLogCommand);
    modelAndView.addObject("message", localeService.getMessage("petlog.created", request));
    petLogCommand = new PetLogCommand();
    modelAndView.addObject("petLogCommand", petLogCommand);
    return fillModelAndView(modelAndView, pets, request);
  }

  private ModelAndView fillModelAndView(
      ModelAndView modelAndView, List<Pet> pets, HttpServletRequest request) {
    modelAndView.addObject("pets", pets);
    if (pets == null) {
      modelAndView.addObject("petListEmpty", localeService.getMessage("pet.list.empty", request));
    }
    return modelAndView;
  }

  @RequestMapping(method = GET, value = "/list")
  public ModelAndView list(@RequestParam("uuid") String uuid) {
    log.info("Listing pet logs");
    ModelAndView modelAndView = new ModelAndView();
    Pet pet = petService.getPetByUuid(uuid);
    List<PetLog> petLogs = petLogService.getPetLogsByPet(pet);
    modelAndView.addObject("petLogs", petLogs);
    modelAndView.addObject("uuid", uuid);
    return modelAndView;
  }

  private List<Pet> getPetsFromUser(Pet pet, User currentUser) {
    List<Pet> pets;
    if (pet.getUser() == currentUser) {
      pets = petService.getPetsByUser(currentUser);
    } else {
      pets = petService.getPetsByUser(pet.getUser());
    }
    return pets;
  }
}
