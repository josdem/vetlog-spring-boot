package com.jos.dem.vetlog.enums

enum CurrentEnvironment {

  DEVELOPMENT("development"), STAGE("stage"), PRODUCTION("production")

  private String description

  CurrentEnvironment(String description){
    this.description = description
  } 	

  String getDescription(){
    description
  }

}