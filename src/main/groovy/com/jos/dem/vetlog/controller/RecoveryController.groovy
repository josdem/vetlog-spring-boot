package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

import com.jos.dem.vetlog.service.RecoveryService

@Controller
@RequestMapping("/recovery")
class RecoveryController {

  @Autowired
  RecoveryService recoveryService

	@RequestMapping(method = GET, value = "/activate/{token}")
	String create(@PathVariable String token){
    recoveryService.confirmAccountForToken(token)
    'login'
	}

}
