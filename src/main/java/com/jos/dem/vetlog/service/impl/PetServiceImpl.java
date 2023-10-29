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

package com.jos.dem.vetlog.service.impl;

import com.jos.dem.vetlog.binder.PetBinder;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetCommand;
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.exception.BusinessException;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.PetAdoption;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.AdoptionRepository;
import com.jos.dem.vetlog.repository.PetRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.LocaleService;
import com.jos.dem.vetlog.service.PetImageService;
import com.jos.dem.vetlog.service.PetService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetBinder petBinder;
    private final PetRepository petRepository;
    private final PetImageService petImageService;
    private final UserRepository userRepository;
    private final AdoptionRepository adoptionRepository;
    private final LocaleService localeService;

    @Transactional
    public Pet save(Command command, User user) throws IOException {
        Pet pet = petBinder.bindPet(command);
        pet.setUser(user);
        petImageService.attachFile(command);
        petRepository.save(pet);
        return pet;
    }

    @Transactional
    public Pet update(Command command) throws IOException {
        PetCommand petCommand = (PetCommand) command;
        recoveryImages(petCommand);
        Pet pet = petBinder.bindPet(petCommand);
        Optional<User> user = getUser(petCommand.getUser());
        if (!user.isPresent()) {
            throw new BusinessException("No user was found with id: " + petCommand.getUser());
        }
        pet.setUser(user.get());
        Optional<User> adopter = getUser(petCommand.getAdopter());
        if (adopter.isPresent()) {
            pet.setAdopter(adopter.get());
        }
        pet.setUser(user.get());
        petImageService.attachFile(petCommand);
        petRepository.save(pet);
        return pet;
    }

    public Pet getPetByUuid(String uuid) {
        return petRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new BusinessException("No pet was found under uuid: " + uuid));
    }

    public Pet getPetById(Long id) {
        Optional<Pet> pet = petRepository.findById(id);
        if (!pet.isPresent()) {
            throw new BusinessException("No pet was found with id: " + id);
        }
        return pet.get();
    }

    public List<Pet> getPetsByUser(User user) {
        List<Pet> result = petRepository.findAllByUser(user);
        result.removeAll(petRepository.findAllByStatus(PetStatus.ADOPTED));
        result.addAll(petRepository.findAllByAdopter(user));
        return result;
    }

    public List<Pet> getPetsByStatus(PetStatus status) {
        return petRepository.findAllByStatus(status);
    }

    @Override
    public void getPetsAdoption(List<Pet> pets) {
        pets.forEach(pet -> {
            Optional<PetAdoption> optional = adoptionRepository.findByPet(pet);
            if (optional.isPresent()) {
                pet.setAdoption(optional.get());
            }
        });
    }

    @Override
    public void deletePetById(Long id) {
        Pet pet =
                petRepository.findById(id).orElseThrow(() -> new BusinessException("No pet was found with id: " + id));
        if (pet.getStatus() == PetStatus.IN_ADOPTION) {
            throw new BusinessException(localeService.getMessage("pet.delete.error.inAdoption"));
        }
        petRepository.delete(pet);
    }

    private void recoveryImages(PetCommand command) {
        Optional<Pet> pet = petRepository.findById(command.getId());
        if (!pet.isPresent()) {
            throw new BusinessException("No pet was found with id: " + command.getId());
        }
        command.setImages(pet.get().getImages());
    }

    private Optional<User> getUser(Long id) {
        return id == null ? Optional.empty() : userRepository.findById(id);
    }
}
