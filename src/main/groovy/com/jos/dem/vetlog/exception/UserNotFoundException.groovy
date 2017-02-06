package com.jos.dem.vetlog.exception

import java.lang.RuntimeException

class UserNotFoundException extends RuntimeException {

  UserNotFoundException(String message){
    super(message)
  }

}
