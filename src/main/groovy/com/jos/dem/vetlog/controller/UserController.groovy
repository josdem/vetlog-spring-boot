package com.jos.dem.vetlog.controller

import static org.springframework.web.bind.annotation.RequestMethod.GET

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.stereotype.Controller

import com.jos.dem.vetlog.command.UserCommand

@Controller
@RequestMapping("/users")
class UserController {

  @RequestMapping(method = GET, value = "/create")
  def create(){
    def modelAndView = new ModelAndView('user/create')
    def userCommand = new UserCommand()
    modelAndView.addObject('userCommand', userCommand)
    modelAndView
  }
  
}