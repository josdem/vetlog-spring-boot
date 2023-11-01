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

import com.jos.dem.vetlog.helper.LocaleResolver;
import com.jos.dem.vetlog.service.LocaleService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocaleServiceImpl implements LocaleService {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public String getMessage(String code, HttpServletRequest request) {
        return messageSource.getMessage(code, null, localeResolver.resolveLocale(request));
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, new Locale("en"));
    }

    public String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }
}
