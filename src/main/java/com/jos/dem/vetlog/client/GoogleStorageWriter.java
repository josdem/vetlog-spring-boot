/*
Copyright 2022 Jose Morales joseluis.delacruz@gmail.com

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

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.jos.dem.vetlog.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class GoogleStorageWriter {

    private static Storage storage = StorageOptions.getDefaultInstance().getService();

    public void uploadToBucket(String bucket, String fileName, InputStream inputStream) throws IOException {
        try {
            storage.create(
                    BlobInfo.newBuilder(bucket, fileName).build(),
                    inputStream.readAllBytes(),
                    Storage.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ)
            );
        } catch (IllegalStateException iee) {
            throw new BusinessException(iee.getMessage());
        }

    }

}
