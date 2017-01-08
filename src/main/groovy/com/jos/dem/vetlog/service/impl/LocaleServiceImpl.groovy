package com.jos.dem.vetlog.service.impl

import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.jos.dem.vetlog.service.LocaleService

@Service
class LocaleServiceImpl implements LocaleService {

  @Autowired
  MessageSource messageSource

  String getMessage(String code){
    messageSource.getMessage(code, null, new Locale("en"))
  }

}
