package com.jos.dem.vetlog.controller

import org.springframework.stereotype.Controller

@controller
class UserController {

  def create(){
    def userCommand = new UserCommand()
    userCommand
  }
  
}