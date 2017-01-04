package com.jos.dem.vetlog.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

import com.jos.dem.vetlog.service.VetlogService

@Controller
class VetlogController {
  
  @RequestMapping("/")
  String index(){
    'index'
  }
}
