package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

import com.jos.dem.vetlog.service.RecoveryService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/recovery")
class RecoveryController {

  @Autowired
  RecoveryService recoveryService

  Logger log = LoggerFactory.getLogger(this.class)

	@RequestMapping(method = GET, value = "/activate/{token}")
	String create(@PathVariable String token){
  	log.info "Calling activate token"
    recoveryService.confirmAccountForToken(token)
    'login/login'
	}

  @RequestMapping("/password")
  ModelAndView login(@RequestParam Optional<String> error){
  	log.info "Calling recovery password"
    ModelAndView modelAndView = new ModelAndView('recovery/recoveryPassword')
    if(error.isPresent()){
      modelAndView.addObject('error', localeService.getMessage('recovery.error'))
    }
    modelAndView
  }

}
