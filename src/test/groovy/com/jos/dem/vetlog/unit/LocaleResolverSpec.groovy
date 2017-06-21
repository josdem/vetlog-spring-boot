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

package com.jos.dem.vetlog.unit

import javax.servlet.http.HttpServletRequest

import com.jos.dem.vetlog.helper.LocaleResolver

import spock.lang.Specification

class LocaleResolverSpec extends Specification {

	LocaleResolver resolver = new LocaleResolver()

	void "should get locale default"(){
		given:'A request'
		  HttpServletRequest request = Mock(HttpServletRequest)
    when:'We call resolve'
      Locale result = resolver.resolveLocale(request)
    then:'We expect default'
      Locale.getDefault() == result
	}

  void "should get en-US as locale"(){
		given:'A request'
		  HttpServletRequest request = Mock(HttpServletRequest)
    when:'We call resolve'
      request.getHeader('Accept-Language') >> 'en-US,en;q=0.8'
      Locale result = resolver.resolveLocale(request)
    then:'We expect default'
      result  == new Locale('en')
	}

  void "should get es-MX as locale"(){
		given:'A request'
		  HttpServletRequest request = Mock(HttpServletRequest)
    when:'We call resolve'
      request.getHeader('Accept-Language') >> 'es-MX,en-US;q=0.7,en;q=0.3'
      Locale result = resolver.resolveLocale(request)
    then:'We expect default'
      result  == new Locale('es')
	}



}
