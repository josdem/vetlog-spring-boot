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
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletResponse

import com.jos.dem.vetlog.enums.PetType
import com.jos.dem.vetlog.service.BreedService

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Controller
@RequestMapping("/breed")
class BreedController {

  @Autowired
  BreedService breedService

  Logger log = LoggerFactory.getLogger(this.class)

  @RequestMapping(method=RequestMethod.GET, value="/list")
  @ResponseBody
  def listByType(@RequestParam String type, HttpServletResponse response){
    log.info "Listing Pets by type: $type"

    response.addHeader("Allow-Control-Allow-Methods", "GET")
    response.addHeader("Access-Control-Allow-Origin", "*")
    breedService.getBreedsByType(PetType.getPetTypeByValue(type))
  }

}
