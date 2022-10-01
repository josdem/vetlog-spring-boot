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

package com.jos.dem.vetlog.service.impl;

import com.jos.dem.vetlog.binder.PetBinder;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetCommand;
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import com.jos.dem.vetlog.repository.PetRepository;
import com.jos.dem.vetlog.repository.UserRepository;
import com.jos.dem.vetlog.service.PetPrescriptionService;
import com.jos.dem.vetlog.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

  private final PetBinder petBinder;
  private final PetRepository petRepository;
  private final PetPrescriptionService petImageService;
  private final UserRepository userRepository;

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
    pet.setUser(user.get());
    petImageService.attachFile(petCommand);
    petRepository.save(pet);
    return pet;
  }

  public Pet getPetByUuid(String uuid) {
    return petRepository.findByUuid(uuid);
  }

  public Pet getPetById(Long id) {
    return petRepository.findById(id).get();
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

  private void recoveryImages(PetCommand command) {
    Pet pet = petRepository.findById(command.getId()).get();
    command.setImages(pet.getImages());
  }

  private Optional<User> getUser(Long id) {
    return userRepository.findById(id);
  }
}
