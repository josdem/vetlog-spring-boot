package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.stereotype.Controller

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.command.AdoptionCommand
import com.jos.dem.vetlog.service.PetService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/adoption")
class AdoptionController {

  @Autowired
  PetService petService

  @Value('${awsImageUrl}')
  String awsImageUrl

  Logger log = LoggerFactory.getLogger(this.class)

  @RequestMapping(method = GET, value = "/descriptionForAdoption")
  ModelAndView descriptionForAdoption(@RequestParam("uuid") String uuid){
    log.info "Adding description to pet with uuid: ${uuid}"
    ModelAndView modelAndView = new ModelAndView()
    Pet pet = petService.getPetByUuid(uuid)
    modelAndView.addObject('pet', pet)
    modelAndView.addObject('adoptionCommand', new AdoptionCommand(uuid:pet.uuid))
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

  @RequestMapping(method = POST, value = "/save")
  String save(@Valid AdoptionCommand adoptionCommand, BindingResult bindingResult) {
    log.info "Creating adption description for pet: ${adoptionCommand.uuid}"
    if (bindingResult.hasErrors()) {
      return "adoption/descriptionForAdoption?uuid=${adoptionCommand.uuid}"
    }
    Pet pet = petService.getPetByUuid(uuid)
    adoptionCommand.pet = pet
    adoptionService.save(adoptionCommand)
    'redirect:/'
  }

}
