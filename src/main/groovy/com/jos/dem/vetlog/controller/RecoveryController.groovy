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
import com.jos.dem.vetlog.command.RecoveryPasswordCommand
import com.jos.dem.vetlog.validator.RecoveryPasswordValidator

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/recovery")
class RecoveryController {

  @Autowired
  RecoveryService recoveryService
  @Autowired
  RecoveryPasswordValidator recoveryPasswordValidator

  Logger log = LoggerFactory.getLogger(this.class)

  @InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.addValidators(recoveryPasswordValidator)
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
  String recoveryPassword(){
  	log.info "Calling recovery password"
    'recovery/recoveryPassword'
  }

}
