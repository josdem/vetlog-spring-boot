package com.jos.dem.vetlog.advice

import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.jos.dem.vetlog.exception.BusinessException

@Aspect
@Component
class AfterThrowingAdvice {

  Logger log = LoggerFactory.getLogger(this.class)

  @AfterThrowing(pointcut = "execution(* com.jos.dem.vetlog.service..**.*(..))", throwing = "ex")
  public void doRecoveryActions(RuntimeException ex) {
    log.info "Wrapping exception ${ex}"
    throw new BusinessException(ex.getMessage(), ex)
  }

}
