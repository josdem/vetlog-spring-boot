package com.jos.dem.vetlog.controller;

import com.jos.dem.vetlog.command.UsernameCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/vet")
public class VetController {

  @GetMapping("/form")
  public ModelAndView search() {
    log.info("Searching pets");
    ModelAndView modelAndView = new ModelAndView("vet/form");
    modelAndView.addObject("usernameCommand", new UsernameCommand());
    return modelAndView;
  }
}
