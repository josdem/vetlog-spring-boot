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
import com.josdem.vetlog.model.Location;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/geolocation")
public class LocationController {

    public static final String DOMAIN = "vetlog.org";

    @GetMapping(value = "/location", consumes = "application/json")
    public ResponseEntity<String> showLocation(
            @RequestBody PetGeolocation petGeolocation, HttpServletResponse response) {
        log.info("Storing location for pet: {}", petGeolocation.getId());

        response.addHeader("Access-Control-Allow-Methods", "GET");
        response.addHeader("Access-Control-Allow-Origin", DOMAIN);

        ApplicationCache.locations.put(
                petGeolocation.getId(), new Location(petGeolocation.getLatitude(), petGeolocation.getLongitude()));

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
