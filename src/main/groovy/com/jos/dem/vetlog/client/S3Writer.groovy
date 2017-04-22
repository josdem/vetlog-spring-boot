package com.jos.dem.vetlog.client

interface S3Writer {
  void uploadToBucket(String bucketDestination, File file)
}
