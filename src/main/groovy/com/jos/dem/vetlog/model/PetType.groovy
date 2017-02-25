package com.jos.dem.vetlog.model

enum PetType {
  CAT('Cat'), DOG('Dog'), BIRD('Bird'), ROEDENT('Roedent'), SPIDER('Spider'), SNAKE('Snake')

  private final String value

  PetType(String value){
    this.value = value
  }

  getValue(){
    value
  }
}
