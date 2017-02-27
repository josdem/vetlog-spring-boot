package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.PetType
import  com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.command.PetCommand
import com.jos.dem.vetlog.validator.PetValidator
import com.jos.dem.vetlog.service.BreedService
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.LocaleService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/pet")
class PetController {

  @Autowired
  PetValidator petValidator
  @Autowired
  BreedService breedService
  @Autowired
  PetService petService
  @Autowired
  UserService userService
  @Autowired
  LocaleService localeService

  @Value('${breedsByTypeUrl}')
  String breedsByTypeUrl

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(petValidator)
  }

  @RequestMapping(method = GET, value = "/create")
  ModelAndView create(){
    def modelAndView = new ModelAndView('pet/create')
    def petCommand = new PetCommand()
    modelAndView.addObject('breeds', breedService.getBreedsByType(PetType.DOG))
    modelAndView.addObject('breedsByTypeUrl', breedsByTypeUrl)
    modelAndView.addObject('petCommand', petCommand)
    modelAndView
  }

  @RequestMapping(method = POST, value = "/save")
  ModelAndView save(@Valid PetCommand command, BindingResult bindingResult) {
    log.info "Creating pet: ${command.name}"
    ModelAndView modelAndView = new ModelAndView('pet/create')
    if (bindingResult.hasErrors()) {
      return modelAndView
    }
    User user = userService.getCurrentUser()
    petService.save(command, user)
    modelAndView.addObject('message', localeService.getMessage('pet.created'))
    modelAndView
  }

  @RequestMapping(method=RequestMethod.GET, value="/list")
  @ResponseBody
  def listByType(@RequestParam String type, HttpServletResponse response){
    log.info "Listing Pets by type: $type"

    response.addHeader("Allow-Control-Allow-Methods", "GET")
    response.addHeader("Access-Control-Allow-Origin", "*")
    breedService.getBreedsByType(PetType.valueOf(type))
  }

}
