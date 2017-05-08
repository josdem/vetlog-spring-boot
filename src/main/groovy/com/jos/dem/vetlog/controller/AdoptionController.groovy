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

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.command.AdoptionCommand
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
  AdoptionService adoptionService

  @Value('${awsImageUrl}')
  String awsImageUrl

  Logger log = LoggerFactory.getLogger(this.class)

  @RequestMapping(method = GET, value = "/descriptionForAdoption")
  ModelAndView descriptionForAdoption(AdoptionCommand adoptionCommand){
    log.info "Adding description to pet with uuid: ${adoptionCommand.uuid}"
    ModelAndView modelAndView = new ModelAndView()
    Pet pet = petService.getPetByUuid(adoptionCommand.uuid)
    modelAndView.addObject('pet', pet)
    modelAndView.addObject('adoptionCommand', adoptionCommand)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

  @RequestMapping(method = POST, value = "/save")
  String save(AdoptionCommand adoptionCommand) {
    log.info "Creating adption description for pet: ${adoptionCommand.uuid}"
    adoptionService.save(adoptionCommand)
    'redirect:/'
  }

}
