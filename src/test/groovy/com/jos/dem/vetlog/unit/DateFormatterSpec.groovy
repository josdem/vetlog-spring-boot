/*
Copyright 2017 José Luis De la Cruz Morales joseluis.delacruz@gmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.jos.dem.vetlog.unit

import com.jos.dem.vetlog.util.DateFormatter
import spock.lang.Specification

class DateFormatterSpec extends Specification {

  DateFormatter formatter = new DateFormatter()

  void "should format date"(){
    given:"A Date"
      String dateToFormat = '2017-05-14 20:30:00'
    when:"We format a date"
      String result = formatter.format(dateToFormat)
    then:"We expect date formatted"
      result == '05/14/2017'
  }
}
