package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller
import javax.validation.Valid

import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.UserCommand
import com.jos.dem.vetlog.validator.UserValidator
import com.jos.dem.vetlog.service.UserService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/user")
class UserController {

  @Autowired
  UserValidator userValidator
  @Autowired
  UserService userService

  Logger log = LoggerFactory.getLogger(this.class)

	@InitBinder('userCommand')
	private void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidator)
	}

	@RequestMapping(method = GET, value = "/create")
	ModelAndView create(){
		Command userCommand = new UserCommand()
    fillUserCommand(userCommand)
	}

	@RequestMapping(method = POST, value = "/save")
	ModelAndView save(@Valid UserCommand userCommand, BindingResult bindingResult) {
    log.info "Saving user: ${userCommand.username}"
    if (bindingResult.hasErrors()) {
      return fillUserCommand(userCommand)
    }
    userService.save(userCommand)
    ModelAndView modelAndView = new ModelAndView('redirect:/')
    modelAndView.addObject('message', localeService.getMessage('user.account.created'))
    modelAndView
  }

  private ModelAndView fillUserCommand(Command userCommand){
		ModelAndView modelAndView = new ModelAndView('user/create')
    modelAndView.addObject('userCommand', userCommand)
		modelAndView
  }

}
