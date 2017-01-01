package com.jos.dem.vetlog.controller

import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.http.HttpStatus
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.ModelAndView

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

@Component
class HandlerException implements HandlerExceptionResolver {

  Log log = LogFactory.getLog(this.class)

  ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
    log.info ex.message
    def data = [:]
    data.message = ex.message
    ModelAndView modelAndView = new ModelAndView("error")
    modelAndView.addObject("data", data)
    modelAndView
  }
}
