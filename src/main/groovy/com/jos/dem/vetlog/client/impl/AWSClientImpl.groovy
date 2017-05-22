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

import com.jos.dem.vetlog.client.AWSClient

@Service
class AWSClientImpl implements AWSClient {

  @Value('${aws.key}')
  String keyId
  @Value('${aws.secret}')
  String keySecret

  AmazonS3 s3Client

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
