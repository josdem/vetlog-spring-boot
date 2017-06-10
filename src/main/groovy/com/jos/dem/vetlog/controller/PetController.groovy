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

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller
import javax.validation.Valid

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.enums.PetType
import com.jos.dem.vetlog.enums.PetStatus
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.PetCommand
import com.jos.dem.vetlog.validator.PetValidator
import com.jos.dem.vetlog.service.BreedService
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.binder.PetBinder

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/pet")
class PetController {

  @Autowired
  BreedService breedService
  @Autowired
  PetService petService
  @Autowired
  UserService userService
  @Autowired
  LocaleService localeService
  @Autowired
  PetBinder petBinder
  @Autowired
  PetValidator petValidator

  @Value('${breedsByTypeUrl}')
  String breedsByTypeUrl
  @Value('${awsImageUrl}')
  String awsImageUrl
  @Value('${defaultImage}')
  String defaultImage

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder('petCommand')
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(petValidator)
  }

  @RequestMapping(method = GET, value = "/create")
  ModelAndView create(@RequestParam (value="type", required=false) String type){
    ModelAndView modelAndView = new ModelAndView('pet/create')
    Command petCommand = new PetCommand()
    modelAndView.addObject('petCommand', petCommand)
    fillModelAndView(modelAndView)
  }

  @RequestMapping(method = GET, value = "/edit")
  ModelAndView edit(@RequestParam("uuid") String uuid) {
    log.info "Editing pet: $uuid"
    Pet pet = petService.getPetByUuid(uuid)
    ModelAndView modelAndView = new ModelAndView()
    modelAndView.addObject('petCommand', petBinder.bindPet(pet))
    modelAndView.addObject('breeds', breedService.getBreedsByType(PetType.DOG))
    modelAndView.addObject('breedsByTypeUrl', breedsByTypeUrl)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

  @RequestMapping(method = POST, value = "/update")
  ModelAndView update(@Valid PetCommand petCommand, BindingResult bindingResult) {
    log.info "Updating pet: ${petCommand.name}"
    ModelAndView modelAndView = new ModelAndView('pet/edit')
    if (bindingResult.hasErrors()) {
      modelAndView.addObject('petCommand', petCommand)
      return fillModelAndView(modelAndView)
    }
    petService.update(petCommand)
    modelAndView.addObject('message', localeService.getMessage('pet.updated'))
    modelAndView.addObject('petCommand', petCommand)
    fillModelAndView(modelAndView)
  }


  @RequestMapping(method = POST, value = "/save")
  ModelAndView save(@Valid PetCommand petCommand, BindingResult bindingResult) {
    log.info "Creating pet: ${petCommand.name}"
    ModelAndView modelAndView = new ModelAndView('pet/create')
    if (bindingResult.hasErrors()) {
      modelAndView.addObject('petCommand', petCommand)
      return fillModelAndView(modelAndView)
    }
    User user = userService.getCurrentUser()
    Pet pet = petService.save(petCommand, user)
    modelAndView.addObject('message', localeService.getMessage('pet.created'))
    petCommand = new PetCommand()
    modelAndView.addObject('petCommand', petCommand)
    fillModelAndView(modelAndView)
  }

  ModelAndView fillModelAndView(ModelAndView modelAndView){
    modelAndView.addObject('breeds', breedService.getBreedsByType(PetType.DOG))
    modelAndView.addObject('breedsByTypeUrl', breedsByTypeUrl)
    modelAndView
  }

  @RequestMapping(method = GET, value = "/list")
  ModelAndView list() {
    log.info 'Listing pets'
    ModelAndView modelAndView = new ModelAndView()
    fillPetAndImageUrl(modelAndView)
  }

  @RequestMapping(method = GET, value = "/giveForAdoption")
  ModelAndView giveForAdoption() {
    log.info 'Select a pet for adoption'
    ModelAndView modelAndView = new ModelAndView('pet/giveForAdoption')
    fillPetAndImageUrl(modelAndView)
  }

  @RequestMapping(method = GET, value = "/listForAdoption")
  ModelAndView listForAdoption() {
    log.info 'Listing pets for adoption'
    ModelAndView modelAndView = new ModelAndView('pet/listForAdoption')
    List<Pet> pets = petService.getPetsByStatus(PetStatus.IN_ADOPTION)
    if(!pets){
      modelAndView.addObject('petListEmpty', localeService.getMessage('pet.list.empty'))
    }
    modelAndView.addObject('pets', pets)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

  private ModelAndView fillPetAndImageUrl(ModelAndView modelAndView){
    User user = userService.getCurrentUser()
    List<Pet> pets = petService.getPetsByUser(user)
    modelAndView.addObject('pets', pets)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView.addObject('defaultImage', defaultImage)
    modelAndView
  }

}
