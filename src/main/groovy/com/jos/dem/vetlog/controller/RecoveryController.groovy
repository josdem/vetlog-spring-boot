package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.servlet.ModelAndView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.stereotype.Controller
import javax.validation.Valid

import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.command.RecoveryPasswordCommand
import com.jos.dem.vetlog.command.ChangePasswordCommand
import com.jos.dem.vetlog.validator.RecoveryPasswordValidator
import com.jos.dem.vetlog.validator.ChangePasswordValidator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/recovery")
class RecoveryController {

  @Autowired
  RecoveryService recoveryService
  @Autowired
  RecoveryPasswordValidator recoveryPasswordValidator
  @Autowired
  ChangePasswordValidator changePasswordValidator
  @Autowired
  LocaleService localeService

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder('recoveryPassword')
	private void initPasswordBinder(WebDataBinder binder) {
		binder.addValidators(recoveryPasswordValidator)
	}

  @InitBinder('changePassword')
	private void initChangeBinder(WebDataBinder binder) {
		binder.addValidators(changePasswordValidator)
	}

	@RequestMapping(method = GET, value = "/activate/{token}")
	String create(@PathVariable String token){
  	log.info "Calling activate token"
    recoveryService.confirmAccountForToken(token)
    'login/login'
	}

  @RequestMapping(method = POST, value = "/password")
  String generateTokenToChangePassword(@Valid RecoveryPasswordCommand command, BindingResult bindingResult){
  	log.info "Calling generate token for changing password"
    if(bindingResult.hasErrors()){
      return 'recovery/recoveryPassword'
    }
    recoveryService.generateRegistrationCodeForEmail(command.email)
    'login/login'
  }

	@RequestMapping(method = GET, value = "/password")
  ModelAndView recoveryPassword(){
  	log.info "Calling recovery password"
    def modelAndView = new ModelAndView('recovery/recoveryPassword')
		def recoveryPasswordCommand = new RecoveryPasswordCommand()
		modelAndView.addObject('recoveryPasswordCommand', recoveryPasswordCommand)
		modelAndView
  }

  @RequestMapping(method = GET, value = "/forgot/{token}")
    ModelAndView changePassword(@PathVariable String token){
    log.info "Calling change password"
    def modelAndView = new ModelAndView('recovery/changePassword')
    Boolean valid = recoveryService.validateToken(token)
    if(!valid){
      modelAndView.addObject('message', localeService.getMessage('recovery.token.error'))
    }
    def changePasswordCommand = new ChangePasswordCommand(token:token)
    modelAndView.addObject('changePasswordCommand', changePasswordCommand)
    modelAndView
  }

  @RequestMapping(method = POST, value = "/change")
  ModelAndView generateTokenToChangePassword(@Valid ChangePasswordCommand command, BindingResult bindingResult){
  	log.info "Calling save and changing password"
    if(bindingResult.hasErrors()){
      return new ModelAndView('recovery/changePassword')
    }
    ModelAndView modelAndView = new ModelAndView('login/login')
    modelAndView.addObject('message', localeService.getMessage('recovery.password.changed'))
    recoveryService.changePassword(command)
    modelAndView
  }

}
