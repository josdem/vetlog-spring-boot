package com.josdem.vetlog.controller;

import com.josdem.vetlog.config.GoogleProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class GoogleMapController {

    private final GoogleProperties googleProperties;

    @GetMapping("/map")
    public String showMap(Model model) {
        model.addAttribute("apiKey", googleProperties.getApiKey());
        return "map/map";
    }
}
