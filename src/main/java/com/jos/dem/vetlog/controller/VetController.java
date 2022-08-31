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

import com.jos.dem.vetlog.command.UsernameCommand;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.service.PetService;
import com.jos.dem.vetlog.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/vet")
public class VetController {

  @Autowired private UserService userService;
  @Autowired private PetService petService;

  @Value("${gcpImageUrl}")
  private String gcpImageUrl;

  @Value("${defaultImage}")
  private String defaultImage;

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @GetMapping("/form")
  ModelAndView form() {
    log.info("Searching pets");
    ModelAndView modelAndView = new ModelAndView("vet/form");
    modelAndView.addObject("usernameCommand", new UsernameCommand());
    return modelAndView;
  }

  @PostMapping("/search")
  ModelAndView search(@Valid UsernameCommand command) {
    log.info("Listing pets");
    ModelAndView modelAndView = new ModelAndView("vet/list");
    User user = userService.getByUsername(command.getUsername());
    List<Pet> pets = petService.getPetsByUser(user);
    modelAndView.addObject("pets", pets);
    modelAndView.addObject("gcpImageUrl", gcpImageUrl);
    modelAndView.addObject("defaultImage", defaultImage);
    return modelAndView;
  }
}
