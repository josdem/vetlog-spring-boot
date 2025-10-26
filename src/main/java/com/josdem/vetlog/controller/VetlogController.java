/*
  Copyright 2025 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.controller;

import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class VetlogController {

    private final PetService petService;

    public VetlogController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("home/home");
    }

    @GetMapping("/adoption")
    public ModelAndView adoption() {
        ModelAndView modelAndView = new ModelAndView("adoption/descriptionForAdoption");
        Pet pet = petService.findAnyPetForAdoption();

        if (pet == null) {
            log.warn("No pet found for adoption. Displaying a placeholder.");
            // Provide a default pet object to avoid NullPointerExceptions in the template
            pet = new Pet();
            pet.setName("No Pet Available");
            pet.setDescription("Currently, there are no pets available for adoption. Please check back later!");
            pet.setImageUrl("/assets/images/default-pet.png"); // Fallback image
        }

        modelAndView.addObject("pet", pet);
        return modelAndView;
    }
}