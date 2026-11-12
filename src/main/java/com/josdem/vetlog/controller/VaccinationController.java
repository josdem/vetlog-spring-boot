/*
 * Copyright 2026 Jose Morales contact@josdem.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.josdem.vetlog.controller;

import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.service.PetService;
import com.josdem.vetlog.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/vaccination")
@RequiredArgsConstructor
public class VaccinationController {

    private final PetService petService;
    private final VaccinationService vaccinationService;

    @GetMapping("/list")
    public ModelAndView list(@RequestParam("uuid") String petUuid) {
        log.info("Listing applied vaccination for pet: {}", petUuid);
        final var pet = petService.getPetByUuid(petUuid);
        final var vaccinations = vaccinationService.getVaccinesByStatus(pet, VaccinationStatus.APPLIED);
        final var modelAndView = new ModelAndView("vaccination/list");
        modelAndView.addObject("vaccinations", vaccinations);
        modelAndView.addObject("uuid", petUuid);
        return modelAndView;
    }
}
