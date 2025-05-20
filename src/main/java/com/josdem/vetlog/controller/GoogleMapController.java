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

import com.josdem.vetlog.cache.ApplicationCache;
import com.josdem.vetlog.command.PetGeolocation;
import com.josdem.vetlog.config.GeolocationProperties;
import com.josdem.vetlog.config.GoogleProperties;
import com.josdem.vetlog.model.Location;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.service.LocaleService;
import com.josdem.vetlog.service.PetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GoogleMapController {

    private final PetService petService;
    private final LocaleService localeService;
    private final GoogleProperties googleProperties;
    private final GeolocationProperties geolocationProperties;

    @GetMapping("/map")
    public String showMap(@RequestParam Optional<Long> id, Model model, HttpServletRequest request) {
        log.info("Pet id: {}", id);
        Location currentPetLocation = null;
        Pet pet = null;
        if(id.isPresent()){
            log.info("Is present");
            pet = petService.getPetById(id.get());
            currentPetLocation = ApplicationCache.locations.get(pet.getId());
            log.info("Current pet location: {}", currentPetLocation);
        }
        var latitude =
                currentPetLocation != null ? currentPetLocation.getLat() : geolocationProperties.getLatitude();
        var longitude =
                currentPetLocation != null ? currentPetLocation.getLng() : geolocationProperties.getLongitude();
        var message = currentPetLocation != null
                ? pet.getName() + localeService.getMessage("map.pet.geolocation", request)
                : localeService.getMessage("map.pet.default", request);
        model.addAttribute("message", message);
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("apiKey", googleProperties.getApiKey());
        return "map/map";
    }
}
