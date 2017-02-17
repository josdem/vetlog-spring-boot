package com.jos.dem.vetlog.config

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.springframework.core.env.Environment

@Component
class Bootstrap implements ApplicationListener<ApplicationReadyEvent> {

  @Autowired
  Environment environment

  @Override
  void onApplicationEvent(final ApplicationReadyEvent event) {
    if(environment.activeProfiles[0] == 'development'){
      println "Loading development environment"
    }
  }

}
