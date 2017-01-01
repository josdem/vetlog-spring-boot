package com.jos.dem.vetlog.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.service.VetlogService

@RestController
class VetlogController {

  @Autowired
  VetlogService vetlogService

  @RequestMapping("/")
  String index(){
    vetlogService.sendRegistration()
  }
}
