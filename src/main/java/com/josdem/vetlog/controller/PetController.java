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

import com.josdem.vetlog.binder.PetBinder;
import com.josdem.vetlog.record.PetRecord;
import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.model.Breed;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.User;
import com.josdem.vetlog.service.BreedService;
import com.josdem.vetlog.service.LocaleService;
import com.josdem.vetlog.service.PetService;
import com.josdem.vetlog.service.UserService;
import com.josdem.vetlog.service.VaccinationService;
import com.josdem.vetlog.validator.PetValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Comparator;
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

    public static final String PET_RECORD = "petRecord";
    public static final String GCP_IMAGE_URL = "gcpImageUrl";
    public static final String MESSAGE = "message";

    private final BreedService breedService;
    private final PetService petService;
    private final UserService userService;
    private final LocaleService localeService;
    private final PetBinder petBinder;
    private final PetValidator petValidator;
    private final VaccinationService vaccinationService;

    @Value("${breedsByTypeUrl}")
    private String breedsByTypeUrl;

    @Value("${gcpUrl}")
    private String gcpUrl;

    @Value("${imageBucket}")
    private String imageBucket;

    @Value("${defaultImage}")
    private String defaultImage;

    @InitBinder("petRecord")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(petValidator);
    }

    @GetMapping(value = "/create")
    public ModelAndView create(@RequestParam(value = "type", required = false) String type) {
        var modelAndView = new ModelAndView("pet/create");
        var petRecord = createPetRecord();
        modelAndView.addObject(PET_RECORD, petRecord);
        return fillModelAndView(modelAndView);
    }

    public PetRecord createPetRecord() {

        PetRecord petRecord = PetRecord.builder()
                .id(null)
                .name(null)
                .birthDate(null)
                .breed(null)
                .sterilized(null)
                .chipId(null)
                .weight(null)
                .unit(null)
                .user(null)
                .adopter(null)
                .uuid(null)
                .type(null)
                .status(PetStatus.OWNED)
                .image(null)
                .images(null)
                .vaccines(null)
                .build();

        return petRecord;
    }

    @GetMapping(value = "/edit")
    public ModelAndView edit(@RequestParam("uuid") String uuid) {
        log.info("Editing pet: {}", uuid);
        var pet = petService.getPetByUuid(uuid);
        var modelAndView = new ModelAndView();
        final PetRecord petRecord = petBinder.bindPet(pet);
        final User adopter = pet.getAdopter();
        modelAndView.addObject(PET_RECORD, petRecord);
        modelAndView.addObject(GCP_IMAGE_URL, gcpUrl + imageBucket + "/");
        return fillModelAndView(modelAndView);
    }

    @PostMapping(value = "/update")
    public ModelAndView update(@Valid PetRecord petRecord, BindingResult bindingResult, HttpServletRequest request)
            throws IOException {
        log.info("Updating pet: {}", petRecord.name());
        var modelAndView = new ModelAndView("pet/edit");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject(PET_RECORD, petRecord);
            return fillModelAndView(modelAndView);
        }
        Pet updatedPet = petService.update(petRecord);
        petRecord = petBinder.bindPet(updatedPet);
        modelAndView.addObject(MESSAGE, localeService.getMessage("pet.updated", request));
        modelAndView.addObject(GCP_IMAGE_URL, gcpUrl + imageBucket + "/");
        modelAndView.addObject(PET_RECORD, petRecord);
        return fillModelAndView(modelAndView);
    }

    @PostMapping(value = "/save")
    public ModelAndView save(@Valid PetRecord petRecord, BindingResult bindingResult, HttpServletRequest request)
            throws IOException {
        log.info("Creating pet: {}", petRecord.name());
        var modelAndView = new ModelAndView("pet/create");
        if (bindingResult.hasErrors()) {
            modelAndView.addObject(PET_RECORD, petRecord);
            return fillModelAndView(modelAndView);
        }
        var user = userService.getCurrentUser();
        petService.save(petRecord, user);
        modelAndView.addObject(MESSAGE, localeService.getMessage("pet.created", request));
        petRecord = createPetRecord();
        modelAndView.addObject(PET_RECORD, petRecord);
        return fillModelAndView(modelAndView);
    }

    private ModelAndView fillModelAndView(ModelAndView modelAndView) {
    	PetRecord petRecord = (PetRecord) modelAndView.getModel().get(PET_RECORD);

        var petType = petRecord.type() == null ? PetType.DOG : petRecord.type();

        modelAndView.addObject(
                "breeds",
                breedService.getBreedsByType(petType).stream()
                        .sorted(Comparator.comparing(Breed::getName))
                        .toList());

        modelAndView.addObject("breedsByTypeUrl", breedsByTypeUrl);
        return modelAndView;
    }

    @GetMapping(value = "/list")
    public ModelAndView list() {
        log.info("Listing pets");
        return fillPetAndImageUrl(new ModelAndView());
    }

    @GetMapping(value = "/giveForAdoption")
    public ModelAndView giveForAdoption() {
        log.info("Select a pet for adoption");
        var modelAndView = new ModelAndView("pet/giveForAdoption");
        return fillPetAndImageUrl(modelAndView);
    }

    @GetMapping(value = "/listForAdoption")
    public ModelAndView listForAdoption(HttpServletRequest request) {
        log.info("Listing pets for adoption");
        var modelAndView = new ModelAndView("pet/listForAdoption");
        var pets = petService.getPetsByStatus(PetStatus.IN_ADOPTION);
        if (pets == null || pets.isEmpty()) {
            modelAndView.addObject("petListEmpty", localeService.getMessage("pet.list.empty", request));
        }
        petService.getPetsAdoption(pets);
        modelAndView.addObject("pets", pets);
        modelAndView.addObject(GCP_IMAGE_URL, gcpUrl + imageBucket + "/");
        return modelAndView;
    }

    @GetMapping(value = "/delete")
    public ModelAndView delete(@RequestParam("uuid") String uuid, HttpServletRequest request) {
        log.info("Deleting pet: {}", uuid);
        var pet = petService.getPetByUuid(uuid);
        petService.deletePetById(pet.getId());
        var modelAndView = new ModelAndView("pet/list");
        modelAndView.addObject(MESSAGE, localeService.getMessage("pet.deleted", request));
        return fillPetAndImageUrl(modelAndView);
    }

    private ModelAndView fillPetAndImageUrl(ModelAndView modelAndView) {
        var user = userService.getCurrentUser();
        var pets = petService.getPetsByUser(user);
        pets.forEach(pet -> pet.setVaccines(vaccinationService.getVaccinesByStatus(pet, VaccinationStatus.PENDING)));
        modelAndView.addObject("pets", pets);
        modelAndView.addObject("default_chip_id", PetRecord.DEFAULT_CHIP_ID);
        modelAndView.addObject(GCP_IMAGE_URL, gcpUrl + imageBucket + "/");
        modelAndView.addObject("defaultImage", defaultImage);
        return modelAndView;
    }
}