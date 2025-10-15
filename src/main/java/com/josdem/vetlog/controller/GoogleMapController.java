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

import com.josdem.vetlog.config.GeolocationProperties;
import com.josdem.vetlog.config.GoogleProperties;
import com.josdem.vetlog.config.VetlogBackendProperties;
import com.josdem.vetlog.model.Location;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.service.LocaleService;
import com.josdem.vetlog.service.LocationService;
import com.josdem.vetlog.service.PetService;
import com.josdem.vetlog.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/map")
@RequiredArgsConstructor
public class GoogleMapController {

    private final PetService petService;
    private final UserService userService;
    private final LocaleService localeService;
    private final LocationService locationService;
    private final GoogleProperties googleProperties;
    private final GeolocationProperties geolocationProperties;
    private final VetlogBackendProperties vetlogBackendProperties;

    @Value("${gcpUrl}")
    private String gcpUrl;

    @Value("${imageBucket}")
    private String imageBucket;

    @Value("${defaultImage}")
    private String defaultImage;

    @GetMapping("/view")
    public String showMap(@RequestParam Optional<Long> id, Model model, HttpServletRequest request) throws IOException {
        log.info("Pet id: {}", id);
        Location currentPetLocation = null;
        Pet pet = null;
        if (id.isPresent()) {
            log.info("Is present");
            pet = petService.getPetById(id.get());
            var call = locationService.getLocation(vetlogBackendProperties.getToken(), pet.getId());
            var result = call.execute();
            currentPetLocation = result.body();
            log.info("Current pet location: {}", currentPetLocation);
        }
        var latitude = currentPetLocation != null && currentPetLocation.getLat() != 0.0
                ? currentPetLocation.getLat()
                : geolocationProperties.getLatitude();
        var longitude = currentPetLocation != null && currentPetLocation.getLng() != 0.0
                ? currentPetLocation.getLng()
                : geolocationProperties.getLongitude();
        var message = currentPetLocation != null
                ? pet.getName() + " " + localeService.getMessage("map.pet.geolocation", request)
                : localeService.getMessage("map.pet.default", request);
        model.addAttribute("message", message);
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        model.addAttribute("apiKey", googleProperties.getApiKey());
        return "map/map";
    }

    @GetMapping(value = "/list")
    public ModelAndView list(ModelAndView modelAndView) {
        log.info("Listing pets");
        var user = userService.getCurrentUser();
        var pets = petService.getPetsByUser(user);
        modelAndView.addObject("pets", pets);
        modelAndView.addObject("gcpImageUrl", gcpUrl + imageBucket + "/");
        modelAndView.addObject("defaultImage", defaultImage);
        return modelAndView;
    }
}
