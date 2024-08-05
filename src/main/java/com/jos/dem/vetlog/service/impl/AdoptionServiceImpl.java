/*
Copyright 2024 Jose Morales contact@josdem.io

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

package com.jos.dem.vetlog.service.impl;

import com.jos.dem.vetlog.command.AdoptionCommand;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.PetAdoption;
import com.jos.dem.vetlog.repository.PetRepository;
import com.jos.dem.vetlog.service.AdoptionService;
import com.jos.dem.vetlog.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdoptionServiceImpl implements AdoptionService {

    private final PetService petService;
    private final PetRepository petRepository;

    public PetAdoption save(Command command) {
        AdoptionCommand adoptionCommand = (AdoptionCommand) command;
        Pet pet = petService.getPetByUuid(adoptionCommand.getUuid());
        PetAdoption petAdoption = new PetAdoption();
        petAdoption.setPet(pet);
        petAdoption.setDescription(adoptionCommand.getDescription());
        pet.setStatus(PetStatus.IN_ADOPTION);
        pet.setAdoption(petAdoption);
        petRepository.save(pet);
        return petAdoption;
    }
}
