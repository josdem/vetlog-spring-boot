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

 package com.josdem.vetlog.service;

import com.josdem.vetlog.model.Pet;
import com.josdem.vetlog.model.Vaccination;
import java.util.List;

public interface VaccinationService {

    /**
     * Saves a vaccination record.
     *
     * @param vaccination the Vaccination entity to be saved.
     */
    void saveVaccination(Vaccination vaccination);

    /**
     * Updates an existing vaccination record.
     *
     * @param vaccination the Vaccination entity with updated data.
     */
    void updateVaccination(Vaccination vaccination);

    /**
     * Deletes a vaccination record by its ID.
     *
     * @param vaccinationId the ID of the Vaccination to be deleted.
     */
    void deleteVaccination(Long vaccinationId);

    /**
     * Retrieves a list of vaccinations for a specific pet.
     *
     * @param pet the Pet entity whose vaccinations are to be retrieved.
     * @return a list of Vaccination records for the given pet.
     */
    List<Vaccination> getVaccinationsByPet(Pet pet);

    /**
     * Retrieves a vaccination by its ID.
     *
     * @param vaccinationId the ID of the Vaccination to be retrieved.
     * @return the Vaccination entity if found, otherwise null.
     */
    Vaccination getVaccinationById(Long vaccinationId);

    /**
     * Updates the status of a vaccination.
     *
     * @param vaccinationId the ID of the Vaccination to be updated.
     * @param status        the new status (e.g., "PENDING" or "APPLIED").
     */
    void updateVaccinationStatus(Long vaccinationId, String status); // New method for updating vaccination status
}
