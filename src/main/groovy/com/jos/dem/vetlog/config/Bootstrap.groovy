package com.jos.dem.vetlog.config

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class Bootstrap implements ApplicationListener<ApplicationReadyEvent> {

  @Override
  void onApplicationEvent(final ApplicationReadyEvent event) {
    println 'Hello World!'
  }

}
