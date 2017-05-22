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

import spock.lang.Specification

import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.enums.RegistrationCodeStatus

class RegistrationCodeSpec extends Specification {

  RegistrationCode registrationCode = new RegistrationCode()

  void "should create a registration code with creation date"(){
    given:"A date"
      Date now = new Date()
    expect:"Date is not null and in the past"
      registrationCode.dateCreated.getTime() - now.getTime() < 10000
      registrationCode.token.size() == 32
      registrationCode.isValid()
  }

  void "should invalidate a token"(){
    when:"We invalidate token"
      registrationCode.status = RegistrationCodeStatus.INVALID
    then:"Is not valid"
      registrationCode.isValid() == false
  }

}
