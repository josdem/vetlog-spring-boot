package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller
import javax.validation.Valid

import com.jos.dem.vetlog.command.PetCommand
import com.jos.dem.vetlog.validator.PetValidator
import com.jos.dem.vetlog.service.BreedService

@Controller
@RequestMapping("/pet")
class PetController {

  @Autowired
  PetValidator petValidator
  @Autowired
  BreedService breedService

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.addValidators(petValidator)
	}

	@RequestMapping(method = GET, value = "/create")
	ModelAndView create(){
		def modelAndView = new ModelAndView('pet/create')
		def petCommand = new PetCommand()
		modelAndView.addObject('petList', breedService.getBreeds())
		modelAndView.addObject('petCommand', petCommand)
		modelAndView
	}

}
