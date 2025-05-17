package com.josdem.vetlog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleMapController {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @GetMapping("/map")
    public String showMap(Model model) {
        model.addAttribute("apiKey", apiKey);
        return "map/map";
    }
}
