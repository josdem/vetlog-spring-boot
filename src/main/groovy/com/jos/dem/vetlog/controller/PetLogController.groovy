package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.stereotype.Controller

import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.command.PetLogCommand

@Controller
class PetLogController {

  @RequestMapping(method = GET, value = "/create")
  ModelAndView create(){
    ModelAndView modelAndView = new ModelAndView('petlog/create')
    Command petLogCommand = new PetLogCommand()
    modelAndView.addObject('petLogCommand', petLogCommand)
    modelAndView
  }

}
