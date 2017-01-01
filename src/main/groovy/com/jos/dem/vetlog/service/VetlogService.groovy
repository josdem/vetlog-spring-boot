package com.jos.dem.vetlog.service

import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.jos.dem.vetlog.exception.VetlogException

@Service
class VetlogService {

  Logger log = LoggerFactory.getLogger(this.class)

  def sendRegistration(){
    log.debug 'Sending email'
    throw new VetlogException()
  }

}
