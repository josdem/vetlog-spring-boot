package com.jos.dem.vetlog.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller

import com.jos.dem.vetlog.service.VetlogService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
class LoginController {
  Logger log = LoggerFactory.getLogger(this.class)

  @RequestMapping("/login")
  String login(){
  	log.info "Calling login"
    'login/login'
  }
}
