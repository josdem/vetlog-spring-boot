package com.jos.dem.vetlog.client

import com.amazonaws.services.s3.model.S3Object

interface AWSClient {
  S3Object getS3Object(String sourceBucket, String sourceKey)
}
