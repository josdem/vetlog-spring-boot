package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.stereotype.Controller

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.service.PetService

@Controller
@RequestMapping("/adoption")
class AdoptionController {

  @Autowired
  PetService petService

  @RequestMapping(method = GET, value = "/descriptionForAdoption")
  ModelAndView descriptionForAdoption(@RequestParam("uuid") String uuid){
    log.info "Adding description to pet with uuid: ${uuid}"
    ModelAndView modelAndView = new ModelAndView()
    Pet pet = petService.getPetByUuid(uuid)
    modelAndView.addObject('pet', pet)
    modelAndView
  }

}
