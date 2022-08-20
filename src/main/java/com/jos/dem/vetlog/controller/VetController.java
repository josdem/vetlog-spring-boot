package com.jos.dem.vetlog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/vet")
public class VetController {

    @GetMapping("/search")
    public ModelAndView search(){
        log.info("Searching pets");
        return new ModelAndView("search");
    }
}
