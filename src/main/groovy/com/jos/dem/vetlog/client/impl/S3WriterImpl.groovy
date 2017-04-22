package com.jos.dem.vetlog.client.impl

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.AmazonServiceException
import com.amazonaws.AmazonClientException

import com.hp.gl.validator.client.S3Copier
import com.hp.gl.validator.client.AWSClient
import com.hp.gl.validator.exception.BusinessException

@Service
class S3CopierImpl implements S3Copier {

  @Autowired
  AWSClient awsClient

  void uploadToBucket(String bucketDestination, String keyName, File file){
    try{
      awsClient.getS3Client().putObject(new PutObjectRequest(bucketDestination, keyName, file))
    } catch (AmazonServiceException ase) {
      throw new BusinessException(ase.message, ase)
    } catch (AmazonClientException ace) {
      throw new BusinessException(ace.message, ace)
    }
  }

}
