/*
Copyright 2023 Jose Morales joseluis.delacruz@gmail.com

Licensed under the Apache License, Version 2.0 (the "License")
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.jos.dem.vetlog.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocaleResolver extends AcceptHeaderLocaleResolver {

    private static final Locale english = new Locale("en");
    private static final Locale spanish = new Locale("es");
    private static final List<Locale> LOCALES = Arrays.asList(english, spanish);

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        log.info("Accept Language: {}", request.getHeader("Accept-Language"));
        if (request.getHeader("Accept-Language") == null) {
            return english;
        }
        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader("Accept-Language"));
        Locale locale = Locale.lookup(list, LOCALES);
        return locale != null ? locale : english;
    }

}

