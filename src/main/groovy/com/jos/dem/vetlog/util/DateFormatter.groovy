package com.jos.dem.vetlog.util

import org.springframework.stereotype.Component

@Component
class DateFormatter {

  String format(String dateToFormat){
    Date date = Date.parse('yyyy-MM-dd HH:mm:ss', dateToFormat)
    date.format( 'yyyy-MM-dd' )
  }

}
