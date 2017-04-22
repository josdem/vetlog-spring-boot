package com.jos.dem.vetlog.client.impl

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.GetObjectRequest

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

import com.hp.s3.reader.client.AWSClient

@Service
class AWSClientImpl implements AWSClient {

  @Value('${key.id}')
  String keyId
  @Value('${key.secret}')
  String keySecret

  @PostConstruct
  void setup(){
    AWSCredentials credentials = new BasicAWSCredentials(keyId, keySecret)
    ClientConfiguration clientConfig = new ClientConfiguration()
    this.s3Client = new AmazonS3Client(credentials, clientConfig)
  }

  S3Object getS3Object(String sourceBucket, String fileName) {
    s3Client.getObject(new GetObjectRequest(sourceBucket, fileName))
  }

}
