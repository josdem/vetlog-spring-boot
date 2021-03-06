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

import static org.springframework.web.bind.annotation.RequestMethod.GET

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.service.VetlogService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
class LoginController {

  @Autowired
  LocaleService localeService

  Logger log = LoggerFactory.getLogger(this.class)

  @RequestMapping(method=GET, value="/login")
  ModelAndView login(@RequestParam Optional<String> error, HttpServletRequest request){
    log.info "Calling login"
    ModelAndView modelAndView = new ModelAndView('login/login')
    if(error.isPresent()){
      modelAndView.addObject('message', localeService.getMessage('login.error', request))
    }
    modelAndView
  }

  @RequestMapping(method=GET, value="/logout")
  String logout(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication()
    new SecurityContextLogoutHandler().logout(request, response, auth)
    return "redirect:/"
  }

}
