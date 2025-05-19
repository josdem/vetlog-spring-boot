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

import com.josdem.vetlog.config.GoogleProperties;
import com.josdem.vetlog.model.Location;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
@RequiredArgsConstructor
public class GoogleMapController {

    private final GoogleProperties googleProperties;

    @GetMapping("/")
    public String showMap(Model model) {
        model.addAttribute("apiKey", googleProperties.getApiKey());
        return "map/map";
    }

    @PostMapping("/location")
    public String showLocation(@Valid Location location, Model model) {
        model.addAttribute("apiKey", googleProperties.getApiKey());
        model.addAttribute("location", location);
        return "map/map";
    }
}
