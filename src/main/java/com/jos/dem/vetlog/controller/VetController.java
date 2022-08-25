package com.jos.dem.vetlog.controller;

import com.jos.dem.vetlog.command.UsernameCommand;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.service.PetService;
import com.jos.dem.vetlog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/vet")
@RequiredArgsConstructor
public class VetController {

  private final UserService userService;
  private final PetService petService;

  @Value("${gcpImageUrl}")
  private String gcpImageUrl;

  @Value("${defaultImage}")
  private String defaultImage;

  @GetMapping("/form")
  public ModelAndView form() {
    log.info("Searching pets");
    ModelAndView modelAndView = new ModelAndView("vet/form");
    modelAndView.addObject("usernameCommand", new UsernameCommand());
    return modelAndView;
  }

  @PostMapping("/search")
  public ModelAndView search(@Valid UsernameCommand command) {
    log.info("Listing pets");
    ModelAndView modelAndView = new ModelAndView("vet/list");
    User user = userService.getByUsername(command.getUsername());
    List<Pet> pets = petService.getPetsByUser(user);
    modelAndView.addObject("pets", pets);
    modelAndView.addObject("gcpImageUrl", gcpImageUrl);
    modelAndView.addObject("defaultImage", defaultImage);
    return modelAndView;
  }
}
