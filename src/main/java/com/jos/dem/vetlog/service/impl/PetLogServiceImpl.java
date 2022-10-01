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

import com.jos.dem.vetlog.binder.PetLogBinder;
import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.command.PetLogCommand;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.PetLog;
import com.jos.dem.vetlog.repository.PetLogRepository;
import com.jos.dem.vetlog.repository.PetRepository;
import com.jos.dem.vetlog.service.PetLogService;
import com.jos.dem.vetlog.service.PetPrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetLogServiceImpl implements PetLogService {

    private final PetLogBinder petLogBinder;
    private final PetLogRepository petLogRepository;
    private final PetRepository petRepository;

    private final PetPrescriptionService petPrescriptionService;

    @Override
    @Transactional
    public PetLog save(Command command) throws IOException {
        PetLogCommand petLogCommand = (PetLogCommand) command;
        PetLog petLog = petLogBinder.bind(petLogCommand);
        Optional<Pet> pet = petRepository.findById(petLogCommand.getPet());
        petLog.setPet(pet.get());
        petPrescriptionService.attachFile(petLogCommand);
        petLogRepository.save(petLog);
        return petLog;
    }

    @Override
    public List<PetLog> getPetLogsByPet(Pet pet) {
        return petLogRepository.getAllByPet(pet);
    }
}
