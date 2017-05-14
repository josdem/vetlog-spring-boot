package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import javax.validation.Valid

import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.model.PetLog
import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.PetLogCommand
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.UserService
import com.jos.dem.vetlog.service.PetLogService
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.validator.PetLogValidator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/petlog")
class PetLogController {

  @Autowired
  PetLogValidator petLogValidator
  @Autowired
  PetService petService
  @Autowired
  PetLogService petLogService
  @Autowired
  UserService userService
  @Autowired
  LocaleService localeService

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(petLogValidator)
  }

  @RequestMapping(method = GET, value = "/create")
  ModelAndView create(){
    ModelAndView modelAndView = new ModelAndView('petlog/create')
    Command petLogCommand = new PetLogCommand()
    modelAndView.addObject('petLogCommand', petLogCommand)
    User user = userService.getCurrentUser()
    List<Pet> pets = petService.getPetsByUser(user)
    fillModelAndView(modelAndView, pets)
  }

  @RequestMapping(method = POST, value = "/save")
  ModelAndView save(@Valid PetLogCommand petLogCommand, BindingResult bindingResult) {
    log.info "Creating pet: ${petLogCommand.pet}"
    ModelAndView modelAndView = new ModelAndView('petlog/create')
    User user = userService.getCurrentUser()
    List<Pet> pets = petService.getPetsByUser(user)
    if (bindingResult.hasErrors()) {
      modelAndView.addObject('petLogCommand', petLogCommand)
      return fillModelAndView(modelAndView, pets)
    }
    petLogService.save(petLogCommand)
    modelAndView.addObject('message', localeService.getMessage('petlog.created'))
    petLogCommand = new PetLogCommand()
    modelAndView.addObject('petLogCommand', petLogCommand)
    fillModelAndView(modelAndView, pets)
  }

  ModelAndView fillModelAndView(ModelAndView modelAndView, List<Pet> pets){
    modelAndView.addObject('pets', pets)
    if(!pets){
      modelAndView.addObject('error', localeService.getMessage('pet.error'))
    }
    modelAndView
  }

  @RequestMapping(method = GET, value = "/list")
  ModelAndView list(@RequestParam("uuid") String uuid) {
    log.info 'Listing pet logs'
    ModelAndView modelAndView = new ModelAndView()
    Pet pet = petService.getPetByUuid(uuid)
    List<PetLog> petLogs = petLogService.getPetLogsByPet(pet)
    modelAndView.addObject('pets', pets)
    modelAndView
  }

}
