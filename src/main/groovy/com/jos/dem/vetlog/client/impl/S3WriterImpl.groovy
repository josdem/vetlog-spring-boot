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
