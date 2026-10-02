/*
  Copyright 2026 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.helper;

import com.josdem.vetlog.enums.VaccinationStatus;
import com.josdem.vetlog.exception.BusinessException;
import com.josdem.vetlog.model.Vaccination;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VaccinationHelper {
    private static final int MAX_MONTHS_TO_APPLY_VACCINE = 6;
    private static final String VACCINE_OUTDATED_MESSAGE =
            "We can not update a vaccine if schedule date is six months or older";

    public void validateVaccinationDate(List<Vaccination> newVaccines) {
        for (Vaccination newVaccine : newVaccines) {
            if (newVaccine.getStatus() == VaccinationStatus.APPLIED
                    && Period.between(LocalDate.now(), newVaccine.getDate()).toTotalMonths()
                            >= MAX_MONTHS_TO_APPLY_VACCINE) {
                throw new BusinessException(VACCINE_OUTDATED_MESSAGE);
            }
        }
    }
}
