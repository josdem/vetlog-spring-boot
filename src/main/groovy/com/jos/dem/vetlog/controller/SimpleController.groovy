package com.jos.dem.vetlog.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping

@RestController
class SimpleController {

  @RequestMapping("/")
  String index(){
    'Hello Vetlog Spring Boot!'
  }
}
