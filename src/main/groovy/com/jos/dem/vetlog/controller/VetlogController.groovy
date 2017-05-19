package com.jos.dem.vetlog.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.servlet.ModelAndView
import org.springframework.stereotype.Controller

import com.jos.dem.vetlog.service.VetlogService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
class VetlogController {
  Logger log = LoggerFactory.getLogger(this.class)

  @RequestMapping("/")
  ModelAndView index(@RequestParam(value="message", required=false) String message){
    ModelAndView modelAndView = new ModelAndView('home/home')
    modelAndView.addObject('message', message)
    modelAndView
  }

}
