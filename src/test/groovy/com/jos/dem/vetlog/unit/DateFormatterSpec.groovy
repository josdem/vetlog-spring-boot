package com.jos.dem.vetlog.unit

import com.jos.dem.vetlog.util.DateFormatter
import spock.lang.Specification

class DateFormatterSpec extends Specification {

  DateFormatter formatter = new DateFormatter()

  void "should format date"(){
    given:"A Date"
      String dateToFormat = '2017-05-04 20:30:00'
    when:"We format a date"
      String result = formatter.format(dateToFormat)
    then:"We expect date formatted"
      result == '2017-05-04'
  }
}
