package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import javax.validation.Valid

import com.jos.dem.vetlog.command.UserCommand
import com.jos.dem.vetlog.validator.UserValidator

@Controller
@RequestMapping("/users")
class UserController {

  @Autowired
  UserValidator userValidator

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidator)
	}

	@RequestMapping(method = GET, value = "/create")
	ModelAndView create(){
		def modelAndView = new ModelAndView('user/create')
		def userCommand = new UserCommand()
		modelAndView.addObject('userCommand', userCommand)
		modelAndView
	}

	@RequestMapping(method = POST, value = "/save")
	String save(@Valid UserCommand command, Errors errors, ModelMap model) {
    if (errors.hasErrors()) {
      return 'user/create'
    }
    'redirect:/'
  }

}
