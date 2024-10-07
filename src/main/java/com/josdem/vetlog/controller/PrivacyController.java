package com.josdem.vetlog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/privacy")
public class PrivacyController {

    @RequestMapping("/show")
    public String index(){
        log.info("Showing privacy page");
        return "privacy/show";
    }

}
