/*
Copyright 2023 Jose Morales joseluis.delacruz@gmail.com

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

package com.jos.dem.vetlog.client;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.spring.core.GcpProjectIdProvider;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.jos.dem.vetlog.exception.BusinessException;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleStorageWriter {

    private final CredentialsProvider credentialsProvider;
    private final GcpProjectIdProvider gcpProjectIdProvider;
    private Storage storage;

    @PostConstruct
    void setup() throws IOException {
        storage = StorageOptions.newBuilder()
                .setProjectId(gcpProjectIdProvider.getProjectId())
                .setCredentials(credentialsProvider.getCredentials())
                .build()
                .getService();
    }

    public void uploadToBucket(String bucket, String fileName, InputStream inputStream, String contentType)
            throws IOException {
        BlobId blobId = BlobId.of(bucket, fileName);
        BlobInfo blobInfo =
                BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        try {
            storage.create(blobInfo, inputStream.readAllBytes());
        } catch (IllegalStateException iee) {
            throw new BusinessException(iee.getMessage());
        }
    }
}
