package com.jos.dem.vetlog.service.impl

import groovyx.net.http.RESTClient
import org.springframework.stereotype.Service
import groovyx.net.http.HttpResponseException

import com.jos.dem.vetlog.command.Command
import com.jos.dem.vetlog.service.RestService
import com.jos.dem.vetlog.exception.RestException

@Service
class RestServiceImpl implements RestService {

  void sendCommand(Command message){
    try{
      def rest = new RESTClient(message.url)
      def response = rest.post(
        path: message.template,
        body: message,
        requestContentType: 'application/json' )
      response.responseData
    } catch(Exception ex) {
      log.warn "Error: ${ex.message}"
      throw new RestException(ex.message)
    }
  }

}
