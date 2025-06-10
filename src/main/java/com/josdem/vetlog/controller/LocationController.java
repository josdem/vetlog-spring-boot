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
import com.josdem.vetlog.model.Location;
import com.josdem.vetlog.service.EmailService;
import com.josdem.vetlog.util.PetSplitter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/geolocation")
public class LocationController {

    public static final String DOMAIN = "vetlog.org";

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/location/{latitude:.+}/{longitude:.+}")
    public ResponseEntity<String> showLocation(
            @PathVariable("latitude") double latitude,
            @PathVariable("longitude") double longitude,
            HttpServletResponse response) {
        log.info("Storing location : {},{}", latitude, longitude);

        response.addHeader("Access-Control-Allow-Methods", "GET");
        response.addHeader("Access-Control-Allow-Origin", DOMAIN);

        var pets = ApplicationCache.locations.keySet();
        pets.forEach(petId -> ApplicationCache.locations.put(petId, new Location(latitude, longitude)));

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping(value = "/store/{pets}")
    public ResponseEntity<String> storePets(@PathVariable("pets") String pets, HttpServletResponse response) {
        log.info("Storing pets: {}", pets);

        response.addHeader("Access-Control-Allow-Methods", "GET");
        response.addHeader("Access-Control-Allow-Origin", DOMAIN);

        var petIds = PetSplitter.split(pets);
        petIds.forEach(id -> {
            ApplicationCache.locations.put(id, new Location(0.0, 0.0)); // Default location
        });

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping(value = "/pullup/{petId}")
    public ResponseEntity<String> sendEmailNotification(@PathVariable("petId") Long petId, HttpServletRequest request) {
        log.info("Sending pulling up email notification for pet: {}", petId);
        var locale = request.getLocale();
        emailService.sendPullingUpEmail(petId, locale);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
