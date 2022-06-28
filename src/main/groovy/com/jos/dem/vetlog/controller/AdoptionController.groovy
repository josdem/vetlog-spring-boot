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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller
import javax.validation.Valid

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.enums.PetStatus

import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.AdoptionService
import com.jos.dem.vetlog.validator.AdoptionValidator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/adoption")
class AdoptionController {

  @Autowired
  PetService petService
  @Autowired
  AdoptionValidator adoptionValidator
  @Autowired
  AdoptionService adoptionService

  @Value('${awsImageUrl}')
  String awsImageUrl

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder('adoptionCommand')
	private void initBinder(WebDataBinder binder) {
		binder.addValidators(adoptionValidator)
	}

  @RequestMapping(method = GET, value = "/descriptionForAdoption")
  ModelAndView descriptionForAdoption(AdoptionCommand adoptionCommand){
    log.info "Adding description to pet with uuid: ${adoptionCommand.uuid}"
    ModelAndView modelAndView = new ModelAndView()
    fillPetAndAdoptionCommand(modelAndView, adoptionCommand)
  }

  @RequestMapping(method = POST, value = "/save")
  ModelAndView save(@Valid AdoptionCommand adoptionCommand, BindingResult bindingResult) {
    log.info "Creating adption description for pet: ${adoptionCommand.uuid}"
    if (bindingResult.hasErrors()) {
      ModelAndView modelAndView = new ModelAndView('adoption/descriptionForAdoption')
      fillPetAndAdoptionCommand(modelAndView, adoptionCommand)
      return modelAndView
    }
    adoptionService.save(adoptionCommand)
    List<Pet> pets = petService.getPetsByStatus(PetStatus.IN_ADOPTION)
    ModelAndView modelAndView = new ModelAndView('pet/listForAdoption')
    modelAndView.addObject('pets', pets)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

  private fillPetAndAdoptionCommand(ModelAndView modelAndView, AdoptionCommand adoptionCommand){
    Pet pet = petService.getPetByUuid(adoptionCommand.uuid)
    modelAndView.addObject('pet', pet)
    modelAndView.addObject('adoptionCommand', adoptionCommand)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

}
