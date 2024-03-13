/*
Copyright  2024 Jose Morales contact@josdem.io

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

package com.jos.dem.vetlog.service;

import com.jos.dem.vetlog.command.Command;
import com.jos.dem.vetlog.enums.PetStatus;
import com.jos.dem.vetlog.model.Pet;
import com.jos.dem.vetlog.model.User;
import java.io.IOException;
import java.util.List;

public interface PetService {
    Pet save(Command command, User user) throws IOException;

    Pet update(Command command) throws IOException;

    Pet getPetByUuid(String uuid);

    Pet getPetById(Long id);

    List<Pet> getPetsByUser(User user);

    List<Pet> getPetsByStatus(PetStatus status);

    void getPetsAdoption(List<Pet> pets);

    void deletePetById(Long id);
}
