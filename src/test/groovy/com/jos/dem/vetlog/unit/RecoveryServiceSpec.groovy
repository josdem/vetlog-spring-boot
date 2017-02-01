package com.jos.dem.vetlog.unit

import spock.lang.Specification

import com.jos.dem.vetlog.service.RecoveryService
import com.jos.dem.vetlog.service.impl.RecoveryServiceImpl
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.repository.RegistrationCodeRepository
import com.jos.dem.vetlog.model.RegistrationCode
import com.jos.dem.vetlog.command.Command

class RecoveryServiceSpec extends Specification {

	RecoveryService recoveryService = new RecoveryServiceImpl()

	RestService restService = Mock(RestService)
	RegistrationCodeRepository repository = Mock(RegistrationCodeRepository)

	def setup(){
		recoveryService.restService = restService
		recoveryService.repository = repository
	}

	void "should send confirmation account token"(){
		 given:"An email"
	     String email = 'josdem@email.com'
	   when:"We call to  send confirmation token"
	     recoveryService.sendConfirmationAccountToken(email)
	   then:"We expect to persiste and send token"  
	   1 * repository.save(_ as RegistrationCode)
	   1 * restService.sendCommand(_ as Command)
 }

}