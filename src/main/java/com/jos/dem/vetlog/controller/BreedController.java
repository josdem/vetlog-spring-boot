/*
Copyright 2022 Jose Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.controller;

import com.jos.dem.vetlog.enums.PetType;
import com.jos.dem.vetlog.model.Breed;
import com.jos.dem.vetlog.service.BreedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/breed")
@RequiredArgsConstructor
public class BreedController {

  private final BreedService breedService;

  @RequestMapping(method = RequestMethod.GET, value = "/list")
  @ResponseBody
  public List<Breed> listByType(@RequestParam String type, HttpServletResponse response) {
    log.info("Listing Pets by type: " + type);

    response.addHeader("Allow-Control-Allow-Methods", "GET");
    response.addHeader("Access-Control-Allow-Origin", "*");
    return breedService.getBreedsByType(PetType.getPetTypeByValue(type));
  }
}
