package com.jos.dem.vetlog.service

import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException

class RestServiceImpl implements RestService {

  String url = 'put an valid url here!'

  void sendCommand(Command message, String template){
    try{
      def rest = new RESTClient(url)
      def response = rest.post(
        path: template,
        body: message,
        requestContentType: 'application/json' )
      response.responseData
    } catch(Exception ex) {
      log.warn "Error: ${ex.message}"
      throw new RestException(ex.message)
    }
  }

}
