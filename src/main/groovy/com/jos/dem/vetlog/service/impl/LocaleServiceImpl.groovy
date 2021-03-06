/*
Copyright 2017 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.service.impl

import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import javax.servlet.http.HttpServletRequest

import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.helper.LocaleResolver

@Service
class LocaleServiceImpl implements LocaleService {

  @Autowired
  MessageSource messageSource
  @Autowired
  LocaleResolver localeResolver

  String getMessage(String code, HttpServletRequest request){
    messageSource.getMessage(code, null, localeResolver.resolveLocale(request))
  }

  String getMessage(String code){
    messageSource.getMessage(code, null, new Locale("en"))
  }

}
