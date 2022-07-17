/*
Copyright 2022 Jose Morales joseluis.delacruz@gmail.com

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
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.model.Breed;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.BreedRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.util.DateFormatter;
import com.jos.dem.vetlog.util.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class PetBinder {

    @Autowired
    private BreedRepository breedRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DateFormatter dateFormatter;


    public Pet bindPet(Command command) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
        PetCommand petCommand = (PetCommand) command;
        Pet pet = new Pet();
        pet.setId(petCommand.getId());
        pet.setUuid(UuidGenerator.generateUuid());
        pet.setName(petCommand.getName());
        pet.setBirthDate(LocalDate.parse(dateFormatter.format(petCommand.getBirthDate()), formatter));
        pet.setDewormed(petCommand.getDewormed());
        pet.setSterilized(petCommand.getSterilized());
        pet.setVaccinated(petCommand.getVaccinated());
        pet.setImages(petCommand.getImages());
        pet.setStatus(PetStatus.OWNED);
        Optional<Breed> breed = breedRepository.findById(petCommand.getBreed());
        pet.setBreed(breed.get());
        return pet;
    }

    public PetCommand bindPet(Pet pet) {
        PetCommand command = new PetCommand();
        command.setId(pet.getId());
        command.setUuid(pet.getUuid());
        command.setName(pet.getName());
        command.setBirthDate(dateFormatter.format(pet.getBirthDate().toString()));
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

    private Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

}
