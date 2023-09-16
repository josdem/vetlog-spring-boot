/*
Copyright 2023 Jose Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.binder;

import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetCommand;
import com.jos.dem.vetlog.exception.BusinessException;
import com.jos.dem.vetlog.model.Breed;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.repository.BreedRepository;
import com.jos.dem.vetlog.util.UuidGenerator;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetBinder {

    private final BreedRepository breedRepository;

    public Pet bindPet(Command command) {
        PetCommand petCommand = (PetCommand) command;
        Pet pet = new Pet();
        pet.setId(petCommand.getId());
        pet.setUuid(UuidGenerator.generateUuid());
        pet.setName(petCommand.getName());
        pet.setBirthDate(LocalDateTime.parse(petCommand.getBirthDate()));
        pet.setDewormed(petCommand.getDewormed());
        pet.setSterilized(petCommand.getSterilized());
        pet.setVaccinated(petCommand.getVaccinated());
        pet.setImages(petCommand.getImages());
        pet.setStatus(petCommand.getStatus());
        Optional<Breed> breed = breedRepository.findById(petCommand.getBreed());
        if (!breed.isPresent()) {
            throw new BusinessException("Breed was not found for pet: " + pet.getName());
        }
        pet.setBreed(breed.get());
        return pet;
    }

    public PetCommand bindPet(Pet pet) {
        PetCommand command = new PetCommand();
        command.setId(pet.getId());
        command.setUuid(pet.getUuid());
        command.setName(pet.getName());
        command.setBirthDate(pet.getBirthDate().toString());
        command.setDewormed(pet.getDewormed());
        command.setSterilized(pet.getSterilized());
        command.setVaccinated(pet.getVaccinated());
        command.setStatus(pet.getStatus());
        command.setImages(pet.getImages());
        command.setBreed(pet.getBreed().getId());
        command.setUser(pet.getUser().getId());
        command.setType(pet.getBreed().getType());
        return command;
    }
}
