package com.jos.dem.vetlog.controller

import com.jos.dem.vetlog.command.UsernameCommand
import com.jos.dem.vetlog.model.Pet
import com.jos.dem.vetlog.model.User
import com.jos.dem.vetlog.service.PetService
import com.jos.dem.vetlog.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

import javax.validation.Valid

@Controller
@RequestMapping("/vet")
class VetController {

    @Autowired
    private UserService userService
    @Autowired
    private PetService petService

    @Value('${gcpImageUrl}')
    String gcpImageUrl;

    @Value('${defaultImage}')
    String defaultImage

    Logger log = LoggerFactory.getLogger(this.class)

    @GetMapping("/form")
    ModelAndView form() {
        log.info("Searching pets")
        ModelAndView modelAndView = new ModelAndView("vet/form")
        modelAndView.addObject("usernameCommand", new UsernameCommand())
        modelAndView
    }

    @PostMapping("/search")
    ModelAndView search(@Valid UsernameCommand command) {
        log.info("Listing pets")
        ModelAndView modelAndView = new ModelAndView("vet/list")
        User user = userService.getByUsername(command.getUsername())
        List<Pet> pets = petService.getPetsByUser(user)
        modelAndView.addObject("pets", pets)
        modelAndView.addObject("gcpImageUrl", gcpImageUrl)
        modelAndView.addObject("defaultImage", defaultImage)
        modelAndView
    }
}
