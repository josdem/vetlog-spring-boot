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

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.AmazonServiceException
import com.amazonaws.AmazonClientException

import com.jos.dem.vetlog.client.S3Writer
import com.jos.dem.vetlog.client.AWSClient
import com.jos.dem.vetlog.exception.BusinessException

@Service
class S3WriterImpl implements S3Writer {

  @Autowired
  AWSClient awsClient

  void uploadToBucket(String bucketDestination, String fileName, InputStream inputStream){
    try{
      ObjectMetadata metadata = new ObjectMetadata()
      awsClient.getS3Client().putObject(new PutObjectRequest(bucketDestination, fileName, inputStream, metadata))
    } catch (AmazonServiceException ase) {
      throw new BusinessException(ase.message, ase)
    } catch (AmazonClientException ace) {
      throw new BusinessException(ace.message, ace)
    }
  }

}
