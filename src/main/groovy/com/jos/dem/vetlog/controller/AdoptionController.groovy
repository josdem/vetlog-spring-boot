package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable
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

  @Value('${awsImageUrl}')
  String awsImageUrl

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.addValidators(new AdoptionValidator())
	}

  @RequestMapping(method = GET, value = "/descriptionForAdoption/{uuid}")
  ModelAndView descriptionForAdoption(@PathVariable("uuid") String uuid){
    log.info "Adding description to pet with uuid: ${uuid}"
    ModelAndView modelAndView = new ModelAndView()
    Pet pet = petService.getPetByUuid(uuid)
    modelAndView.addObject('pet', pet)
    modelAndView.addObject('adoptionCommand', new AdoptionCommand(uuid:uuid))
    modelAndView.addObject('awsImageUrl', awsImageUrl)
    modelAndView
  }

  @RequestMapping(method = POST, value = "/save")
  String save(@Valid AdoptionCommand adoptionCommand, BindingResult bindingResult) {
    log.info "Creating adption description for pet: ${adoptionCommand.uuid}"
    if (bindingResult.hasErrors()) {
      return "adoption/descriptionForAdoption?uuid=${adoptionCommand.uuid}"
    }
    adoptionService.save(adoptionCommand)
    'redirect:/'
  }

}
