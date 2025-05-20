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
import com.josdem.vetlog.config.GeolocationProperties;
import com.josdem.vetlog.config.GoogleProperties;
import com.josdem.vetlog.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class GoogleMapController {

    private final PetService petService;
    private final GoogleProperties googleProperties;
    private final GeolocationProperties geolocationProperties;

    @GetMapping("/map")
    public String showMap(@RequestParam("id") Long id, Model model) {
        var pet = petService.getPetById(id);
        var currentPetGeolocation = ApplicationCache.locations.get(pet.getId());
        var latitude =
                currentPetGeolocation != null ? currentPetGeolocation.getLat() : geolocationProperties.getLatitude();
        var longitude =
                currentPetGeolocation != null ? currentPetGeolocation.getLng() : geolocationProperties.getLongitude();
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("apiKey", googleProperties.getApiKey());
        return "map/map";
    }
}
