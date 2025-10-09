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

package com.josdem.vetlog.service.impl;

import com.josdem.vetlog.binder.PetBinder;
import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.User;
import com.josdem.vetlog.record.IRecord;
import com.josdem.vetlog.record.PetRecord;
import com.josdem.vetlog.repository.AdoptionRepository;
import com.josdem.vetlog.repository.PetRepository;
import com.josdem.vetlog.repository.UserRepository;
import com.josdem.vetlog.service.LocaleService;
import com.josdem.vetlog.service.PetImageService;
import com.josdem.vetlog.service.PetLogService;
import com.josdem.vetlog.service.PetService;
import com.josdem.vetlog.service.VaccinationService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    public static final String NO_USER_WAS_FOUND_WITH_ID = "No user was found with id: ";
    public static final String NO_PET_WAS_FOUND_WITH_ID = "No pet was found with id: ";
    private final PetBinder petBinder;
    private final PetRepository petRepository;
    private final PetImageService petImageService;
    private final UserRepository userRepository;
    private final AdoptionRepository adoptionRepository;
    private final LocaleService localeService;
    private final VaccinationService vaccinationService;
    private final PetLogService petLogService;

    @Transactional
    public Pet save(IRecord record, User user) throws IOException {
        var pet = petBinder.bindPet(record);
        pet.setUser(user);
        petImageService.attachFile(record);
        petRepository.save(pet);
        vaccinationService.save(pet);
        return pet;
    }

    @Transactional
    public Pet update(IRecord record) throws IOException {
        var petRecord = (PetRecord) record;

        var pet = petBinder.bindPet(petRecord);
        var user = getUser(petRecord.user());
        user.ifPresentOrElse(pet::setUser, () -> {
            throw new BusinessException(NO_USER_WAS_FOUND_WITH_ID + petRecord.user());
        });
        var adopter = getUser(petRecord.adopter());
        adopter.ifPresent(pet::setAdopter);
        user.ifPresent(pet::setUser);
        var petImg = recoveryImages(petRecord.id());
        petImg.ifPresentOrElse(value -> pet.setImages(value.getImages()), () -> {
            throw new BusinessException(NO_PET_WAS_FOUND_WITH_ID + petRecord.id());
        });
        petImageService.attachFile(petRecord);
        petRepository.save(pet);
        return pet;
    }

    public Pet getPetByUuid(String uuid) {
        return petRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new BusinessException("No pet was found under uuid: " + uuid));
    }

    public Pet getPetById(Long id) {
        var pet = petRepository.findById(id);
        return pet.orElseThrow(() -> new BusinessException(NO_PET_WAS_FOUND_WITH_ID + id));
    }

    public List<Pet> getPetsByUser(User user) {
        var result = petRepository.findAllByUser(user);
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
            var optional = adoptionRepository.findByPet(pet);
            optional.ifPresent(pet::setAdoption);
        });
    }

    @Transactional
    public void deletePetById(Long id) {
        var pet = petRepository.findById(id).orElseThrow(() -> new BusinessException(NO_PET_WAS_FOUND_WITH_ID + id));
        if (pet.getStatus() == PetStatus.IN_ADOPTION) {
            throw new BusinessException(localeService.getMessage("pet.delete.error.inAdoption"));
        }
        if (!petLogService.getPetLogsByPet(pet).isEmpty()) {
            throw new BusinessException(localeService.getMessage("pet.delete.error.logs"));
        }
        vaccinationService.deleteVaccinesByPet(pet);
        petRepository.delete(pet);
    }

    private Optional<Pet> recoveryImages(Long id) {

        return id == null ? Optional.empty() : petRepository.findById(id);
    }

    private Optional<User> getUser(Long id) {
        return id == null ? Optional.empty() : userRepository.findById(id);
    }
}
