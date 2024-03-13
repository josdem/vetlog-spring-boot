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

import com.jos.dem.vetlog.binder.PetBinder;
import com.jos.dem.vetlog.command.PetCommand;
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.enums.PetType;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.service.BreedService;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.PetService;
import com.jos.dem.vetlog.service.UserService;
import com.jos.dem.vetlog.validator.PetValidator;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {

    private final BreedService breedService;
    private final PetService petService;
    private final UserService userService;
    private final LocaleService localeService;
    private final PetBinder petBinder;
    private final PetValidator petValidator;

    @Value("${breedsByTypeUrl}")
    private String breedsByTypeUrl;

    @Value("${gcpUrl}")
    private String gcpUrl;

    @Value("${imageBucket}")
    private String imageBucket;

    @Value("${defaultImage}")
    private String defaultImage;

    @InitBinder("petCommand")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(petValidator);
    }

    @GetMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "type", required = false) String type) {
        ModelAndView modelAndView = new ModelAndView("pet/create");
        PetCommand petCommand = new PetCommand();
        petCommand.setStatus(PetStatus.OWNED);
        modelAndView.addObject("petCommand", petCommand);
        return fillModelAndView(modelAndView);
    }

    @GetMapping(value = "/edit")
    public ModelAndView edit(@RequestParam("uuid") String uuid) {
        log.info("Editing pet: {}", uuid);
        Pet pet = petService.getPetByUuid(uuid);
        ModelAndView modelAndView = new ModelAndView();
        final PetCommand petCommand = petBinder.bindPet(pet);
        final User adopter = pet.getAdopter();
        if (adopter != null) {
            petCommand.setAdopter(pet.getAdopter().getId());
        }
        modelAndView.addObject("petCommand", petCommand);
        modelAndView.addObject(
                "breeds", breedService.getBreedsByType(pet.getBreed().getType()));
        modelAndView.addObject("gcpImageUrl", gcpUrl + imageBucket + "/");
        modelAndView.addObject("breedsByTypeUrl", breedsByTypeUrl);
        return modelAndView;
    }

    @PostMapping(value = "/update")
    public ModelAndView update(@Valid PetCommand petCommand, BindingResult bindingResult, HttpServletRequest request)
            throws IOException {
        log.info("Updating pet: {}", petCommand.getName());
        ModelAndView modelAndView = new ModelAndView("pet/edit");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("petCommand", petCommand);
            return fillModelAndView(modelAndView);
        }
        petService.update(petCommand);
        modelAndView.addObject("message", localeService.getMessage("pet.updated", request));
        modelAndView.addObject("gcpImageUrl", gcpUrl + imageBucket + "/");
        modelAndView.addObject("petCommand", petCommand);
        return fillModelAndView(modelAndView);
    }

    @PostMapping(value = "/save")
    public ModelAndView save(@Valid PetCommand petCommand, BindingResult bindingResult, HttpServletRequest request)
            throws IOException {
        log.info("Creating pet: {}", petCommand.getName());
        ModelAndView modelAndView = new ModelAndView("pet/create");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("petCommand", petCommand);
            return fillModelAndView(modelAndView);
        }
        User user = userService.getCurrentUser();
        petService.save(petCommand, user);
        modelAndView.addObject("message", localeService.getMessage("pet.created", request));
        petCommand = new PetCommand();
        modelAndView.addObject("petCommand", petCommand);
        return fillModelAndView(modelAndView);
    }

    private ModelAndView fillModelAndView(ModelAndView modelAndView) {
        modelAndView.addObject("breeds", breedService.getBreedsByType(PetType.DOG));
        modelAndView.addObject("breedsByTypeUrl", breedsByTypeUrl);
        return modelAndView;
    }

    @GetMapping(value = "/list")
    public ModelAndView list() {
        log.info("Listing pets");
        ModelAndView modelAndView = new ModelAndView();
        return fillPetAndImageUrl(modelAndView);
    }

    @GetMapping(value = "/giveForAdoption")
    public ModelAndView giveForAdoption() {
        log.info("Select a pet for adoption");
        ModelAndView modelAndView = new ModelAndView("pet/giveForAdoption");
        return fillPetAndImageUrl(modelAndView);
    }

    @GetMapping(value = "/listForAdoption")
    public ModelAndView listForAdoption(HttpServletRequest request) {
        log.info("Listing pets for adoption");
        ModelAndView modelAndView = new ModelAndView("pet/listForAdoption");
        List<Pet> pets = petService.getPetsByStatus(PetStatus.IN_ADOPTION);
        if (pets == null || pets.isEmpty()) {
            modelAndView.addObject("petListEmpty", localeService.getMessage("pet.list.empty", request));
        }
        petService.getPetsAdoption(pets);
        modelAndView.addObject("pets", pets);
        modelAndView.addObject("gcpImageUrl", gcpUrl + imageBucket + "/");
        return modelAndView;
    }

    @GetMapping(value = "/delete")
    public ModelAndView delete(@RequestParam("uuid") String uuid) {
        log.info("Deleting pet: {}", uuid);
        Pet pet = petService.getPetByUuid(uuid);
        petService.deletePetById(pet.getId());
        ModelAndView modelAndView = new ModelAndView("pet/list");
        return fillPetAndImageUrl(modelAndView);
    }

    private ModelAndView fillPetAndImageUrl(ModelAndView modelAndView) {
        User user = userService.getCurrentUser();
        List<Pet> pets = petService.getPetsByUser(user);
        modelAndView.addObject("pets", pets);
        modelAndView.addObject("gcpImageUrl", gcpUrl + imageBucket + "/");
        modelAndView.addObject("defaultImage", defaultImage);
        return modelAndView;
    }
}
