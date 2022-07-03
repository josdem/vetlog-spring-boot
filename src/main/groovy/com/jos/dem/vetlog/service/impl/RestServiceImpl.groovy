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

import com.jos.dem.vetlog.command.Command
import groovyx.net.http.RESTClient
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.exception.RestException

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class RestServiceImpl implements RestService {

  Logger log = LoggerFactory.getLogger(this.class)

  @Value('${emailer.url}')
  String emailerUrl
  @Value('${emailer.path}')
  String emailerPath
  @Value('${token}')
  String token

  void sendCommand(Command message){
    try{
      def rest = new RESTClient(emailerUrl)
      def response = rest.post(
        path: emailerPath,
        headers: [Authorization:"${token}"],
        body: message,
        requestContentType: 'application/json' )
      response.responseData
    } catch(Exception ex) {
      log.warn "Error: ${ex.message}"
      throw new RestException(ex.message)
    }
  }

}
