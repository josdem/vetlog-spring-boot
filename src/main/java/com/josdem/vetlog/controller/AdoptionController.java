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

import com.josdem.vetlog.record.AdoptionRecord;
import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.service.AdoptionService;
import com.josdem.vetlog.service.PetService;
import com.josdem.vetlog.validator.AdoptionValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/adoption")
@RequiredArgsConstructor
public class AdoptionController {

    private final PetService petService;
    private final AdoptionService adoptionService;
    private final AdoptionValidator adoptionValidator;

    @Value("${gcpUrl}")
    private String gcpUrl;

    @Value("${imageBucket}")
    private String imageBucket;

    @InitBinder("adoptionRecord")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(adoptionValidator);
    }

    @GetMapping(value = "/descriptionForAdoption")
    public ModelAndView descriptionForAdoption(AdoptionRecord adoptionRecord) {
        log.info("Adding description to pet with uuid: {}", adoptionRecord.uuid());
        return fillPetAndAdoptionRecord(new ModelAndView(), adoptionRecord);
    }

    @PostMapping(value = "/save")
    public ModelAndView save(@Valid AdoptionRecord adoptionRecord, BindingResult bindingResult) {
        log.info("Creating adoption description for pet: {}", adoptionRecord.uuid());
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("adoption/descriptionForAdoption");
            fillPetAndAdoptionRecord(modelAndView, adoptionRecord);
            return modelAndView;
        }
        adoptionService.save(adoptionRecord);
        var pets = petService.getPetsByStatus(PetStatus.IN_ADOPTION);
        var modelAndView = new ModelAndView("pet/listForAdoption");
        modelAndView.addObject("pets", pets);
        modelAndView.addObject("gcpImageUrl", gcpUrl + imageBucket + "/");
        return modelAndView;
    }

    private ModelAndView fillPetAndAdoptionRecord(ModelAndView modelAndView, AdoptionRecord adoptionRecord) {
        var pet = petService.getPetByUuid(adoptionRecord.uuid());
        modelAndView.addObject("pet", pet);
        modelAndView.addObject("adoptionRecord", adoptionRecord);
        modelAndView.addObject("gcpImageUrl", gcpUrl + imageBucket + "/");
        return modelAndView;
    }
}
