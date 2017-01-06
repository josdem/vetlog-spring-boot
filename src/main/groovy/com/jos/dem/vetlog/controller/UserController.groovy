package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller
import javax.validation.Valid

import com.jos.dem.vetlog.command.UserCommand
import com.jos.dem.vetlog.validator.UserValidator

@Controller
@RequestMapping("/users")
class UserController {
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(new UserValidator());
	}

	@RequestMapping(method = GET, value = "/create")
	def create(){
		def modelAndView = new ModelAndView('user/create')
		def userCommand = new UserCommand()
		modelAndView.addObject('userCommand', userCommand)
		modelAndView
	}

	@RequestMapping(method = POST, value = "/save")
	def save(@Valid UserCommand command, BindingResult result) {
    if (bindingResult.hasErrors()) {
      def modelAndView =  new ModelAndView("user/create","userCommand", command)
      def mapErrors = []
      bindingResult.getFieldErrors().each{ error ->
        mapErrors.add("${error.field} ${ error.defaultMessage}")
      }
      modelAndView.addObject("error", mapErrors.flatten())
      return modelAndView
    }
    new ModelAndView("redirect:/")
  }

}