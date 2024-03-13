/*
Copyright  2024 Jose Morales contact@josdem.io

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

package com.jos.dem.vetlog.controller;

import com.jos.dem.vetlog.command.TelephoneCommand;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.PetService;
import com.jos.dem.vetlog.service.TelephoneService;
import com.jos.dem.vetlog.service.UserService;
import com.jos.dem.vetlog.validator.TelephoneValidator;
import jakarta.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/telephone")
@RequiredArgsConstructor
public class TelephoneController {

    private final PetService petService;
    private final UserService userService;
    private final LocaleService localeService;
    private final TelephoneService telephoneService;
    private final TelephoneValidator telephoneValidator;

    @Value("${gcpUrl}")
    private String gcpUrl;

    @Value("${imageBucket}")
    private String imageBucket;

    @InitBinder("telephoneCommand")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(telephoneValidator);
    }

    @PostMapping(value = "/save")
    public ModelAndView save(
            @Valid TelephoneCommand telephoneCommand, BindingResult bindingResult, HttpServletRequest request) {
        log.info("Saving adoption for pet: " + telephoneCommand.getUuid());
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("telephone/adopt");
            return fillPetAndTelephoneCommand(modelAndView, telephoneCommand);
        }
        User user = userService.getCurrentUser();
        telephoneService.save(telephoneCommand, user);
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        modelAndView.addObject("message", localeService.getMessage("adoption.email.sent", request));
        return modelAndView;
    }

    @GetMapping(value = "/adopt")
    public ModelAndView adopt(TelephoneCommand telephoneCommand) {
        log.info("Adoption to pet with uuid: " + telephoneCommand.getUuid());
        ModelAndView modelAndView = new ModelAndView("telephone/adopt");
        return fillPetAndTelephoneCommand(modelAndView, telephoneCommand);
    }

    private ModelAndView fillPetAndTelephoneCommand(ModelAndView modelAndView, TelephoneCommand telephoneCommand) {
        Pet pet = petService.getPetByUuid(telephoneCommand.getUuid());
        modelAndView.addObject("pet", pet);
        modelAndView.addObject("telephoneCommand", telephoneCommand);
        modelAndView.addObject("gcpImageUrl", gcpUrl + imageBucket + "/");
        return modelAndView;
    }
}
