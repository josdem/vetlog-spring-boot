/*
Copyright 2017 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.jos.dem.vetlog.controller

import com.jos.dem.vetlog.exception.BusinessException
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.stereotype.Controller


import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
class VetlogController {
  Logger log = LoggerFactory.getLogger(this.class)

  @RequestMapping("/")
  ModelAndView index(@RequestParam(value="message", required=false) String message){
    ModelAndView modelAndView = new ModelAndView('home/home')
    throw new BusinessException("This is an expected error!")
    modelAndView.addObject('message', message)
    modelAndView
  }

}
