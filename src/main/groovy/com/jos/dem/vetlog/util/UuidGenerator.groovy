package com.jos.dem.vetlog.util

class UuidGenerator {
  static String generateUuid() {
    UUID.randomUUID().toString().replaceAll('-', '')
  }
}
