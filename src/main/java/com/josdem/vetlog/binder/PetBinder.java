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

package com.josdem.vetlog.binder;

import com.josdem.vetlog.enums.PetStatus;
import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Breed;
import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.record.IRecord;
import com.josdem.vetlog.record.PetRecord;
import com.josdem.vetlog.repository.BreedRepository;
import com.josdem.vetlog.repository.VaccinationRepository;
import com.josdem.vetlog.service.VaccinationService;
import com.josdem.vetlog.util.UuidGenerator;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetBinder {

    private final BreedRepository breedRepository;
    private final VaccinationService vaccinationService;
    private final VaccinationRepository vaccinationRepository;

    private static final String RABIES_VACCINE = "Rabies";

    public Pet bindPet(IRecord record) {
        PetRecord petRecord = (PetRecord) record;
        Pet pet = new Pet();
        pet.setId(petRecord.id());
        pet.setUuid(UuidGenerator.generateUuid());
        if (petRecord.uuid() != null) {
            pet.setUuid(petRecord.uuid());
        }
        pet.setName(petRecord.name());
        if (petRecord.birthDate().isEmpty()) {
            pet.setBirthDate(LocalDate.now());
        } else {
            pet.setBirthDate(LocalDate.parse(petRecord.birthDate()));
        }
        pet.setSterilized(petRecord.sterilized());
        pet.setImages(petRecord.images());
        pet.setStatus(petRecord.status());

        vaccinationService.updateVaccinations(petRecord, pet);

        /// Save updated vaccines
        pet.setVaccines(petRecord.vaccines());
        petRecord.vaccines().forEach(vaccine -> {
            vaccine.setDate(LocalDate.now());
            vaccinationRepository.save(vaccine);
        });

        Optional<Breed> breed = breedRepository.findById(petRecord.breed());
        if (breed.isEmpty()) {
            throw new BusinessException("Breed was not found for pet: " + pet.getName());
        }
        pet.setBreed(breed.get());
        return pet;
    }

    public PetRecord bindPet(Pet pet) {
        PetRecord petRecord;
        var vaccines = vaccinationRepository.findAllByPet(pet).stream()
                .filter(vaccine -> vaccine.getStatus().equals(VaccinationStatus.PENDING))
                .toList();

        // 1. Long id, 2. @Size(min = 1, max = 50) String name,
        // 3. @NotNull String birthDate,4. Boolean sterilized
        // 5. @Min(1L) Long breed,6.  @Min(1L) Long user,
        // 7. @Min(1L)  Long adopter,8.@NotNull  PetType type
        // 9. String uuid,10. PetStatus status,11. MultipartFile image
        // 12. List<PetImage> images,13. List<Vaccination> vaccines

        if (pet.getAdopter() != null) {

            petRecord = new PetRecord(
                    pet.getId(),
                    pet.getName(),
                    pet.getBirthDate().toString(),
                    pet.getSterilized(),
                    pet.getBreed().getId(),
                    pet.getUser().getId(),
                    pet.getAdopter().getId(),
                    pet.getBreed().getType(),
                    pet.getUuid(),
                    PetStatus.OWNED,
                    null,
                    pet.getImages(),
                    vaccines);

        } else {
            petRecord = new PetRecord(
                    pet.getId(),
                    pet.getName(),
                    pet.getBirthDate().toString(),
                    pet.getSterilized(),
                    pet.getBreed().getId(),
                    pet.getUser().getId(),
                    1L,
                    pet.getBreed().getType(),
                    pet.getUuid(),
                    PetStatus.OWNED,
                    null,
                    pet.getImages(),
                    vaccines);
        }

        return petRecord;
    }
}
