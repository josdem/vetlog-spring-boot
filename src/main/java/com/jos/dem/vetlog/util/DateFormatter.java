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

package com.jos.dem.vetlog.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class DateFormatter {

    private DateTimeFormatter formatter;

    public String format(String dateToFormat) {
        if (dateToFormat.contains(",")) {
            formatter = DateTimeFormatter.ofPattern("M/d/yy, h:m a");
        } else {
            formatter = DateTimeFormatter.ofPattern("d/M/yy H:m");
        }

        LocalDate localDate = LocalDate.parse(dateToFormat, formatter);
        return localDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }
}
