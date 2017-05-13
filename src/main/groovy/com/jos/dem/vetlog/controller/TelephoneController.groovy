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
@RequestMapping("/telephone")
class AdoptionController {

  @Autowired
  PetService petService
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
  ModelAndView save(@Valid TelephoneCommand telephoneCommand, BindingResult bindingResult) {
    log.info "Saving adoption for pet: ${telephoneCommand.uuid}"
    if (bindingResult.hasErrors()) {
      ModelAndView modelAndView = new ModelAndView('telephone/adopt')
      fillPetAndAdoptionCommand(modelAndView, adoptionCommand)
      return modelAndView
    }
    new ModelAndView('redirect:/')
  }

  @RequestMapping(method = GET, value = "/adopt")
  ModelAndView adopt(TelephoneCommand telephoneCommand){
    log.info "Adding description to pet with uuid: ${telephoneCommand.uuid}"
    ModelAndView modelAndView = new ModelAndView('adoption/adopt')
    Pet pet = petService.getPetByUuid(adoptionCommand.uuid)
    modelAndView.addObject('pet', pet)
    modelAndView.addObject('telephoneCommand', telephoneCommand)
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

}
