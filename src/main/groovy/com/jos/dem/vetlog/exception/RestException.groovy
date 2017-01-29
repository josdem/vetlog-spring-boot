package com.jos.dem.vetlog.exception

import java.lang.RuntimeException

class RestException extends RuntimeException {

  RestException(String message){
    super(message)
  }

  @Override
  String getMessage() {
    'Rest exception'
  }

}
