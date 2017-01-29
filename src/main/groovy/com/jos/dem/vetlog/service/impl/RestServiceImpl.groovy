package com.jos.dem.vetlog.service.impl

import groovyx.net.http.RESTClient
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value
import groovyx.net.http.HttpResponseException

import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.exception.RestException

import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class RestServiceImpl implements RestService {

  Logger log = LoggerFactory.getLogger(this.class)

  @Value('${emailerUrl}')
  String emailerUrl
  @Value('${emailerPath}')
  String emailerPath

  void sendCommand(Command message){
    try{
      def rest = new RESTClient(emailerUrl)
      def response = rest.post(
        path: emailerPath,
        body: message,
        requestContentType: 'application/json' )
      response.responseData
    } catch(Exception ex) {
      log.warn "Error: ${ex.message}"
      throw new RestException(ex.message)
    }
  }

}
