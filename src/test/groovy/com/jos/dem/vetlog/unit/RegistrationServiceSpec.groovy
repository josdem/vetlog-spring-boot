/*
Copyright 2022 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

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

import com.jos.dem.vetlog.exception.VetlogException
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.repository.RegistrationCodeRepository
import com.jos.dem.vetlog.service.LocaleService
import com.jos.dem.vetlog.service.RegistrationService
import com.jos.dem.vetlog.service.impl.RegistrationServiceImpl
import spock.lang.Specification

class RegistrationServiceSpec extends Specification {

    RegistrationService registrationService = new RegistrationServiceImpl()

    RegistrationCodeRepository repository = Mock(RegistrationCodeRepository)
    LocaleService localeService = Mock(LocaleService)

    def setup() {
        registrationService.repository = repository
        registrationService.localeService = localeService
    }

    void "should find email by token"() {
        given: "An token"
        String token = 'token'
        and: "A registration code"
        RegistrationCode registrationCode = new RegistrationCode(email: 'josdem@email.com')
        when: "We find registration code by token"
        repository.findByToken(token) >> registrationCode
        String result = registrationService.findEmailByToken(token)
        then: "We expect email"
        'josdem@email.com' == result
    }

    void "should not find email by token since token does not exist"() {
        given: "An token"
        String token = 'token'
        when: "We find registration code by token"
        localeService.getMessage('exception.token.not.found') >> 'Token not found'
        String result = registrationService.findEmailByToken(token)
        then: "We expect exception"
        thrown VetlogException
    }

    void "should generate token"() {
        given: "An email"
        String email = 'josdem@email.com'
        and: "A registration code"
        RegistrationCode registrationCode = new RegistrationCode()
        when: "We generate token"
        repository.save(_ as RegistrationCode) >> registrationCode
        String result = registrationService.generateToken(email)
        then: "We expect token generated"
        result.size() == 36
    }

}
