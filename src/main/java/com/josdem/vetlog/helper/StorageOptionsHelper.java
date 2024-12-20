package com.josdem.vetlog.helper;

import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Component;

@Component
public class StorageOptionsHelper {

    public StorageOptions.Builder getStorageOptions() {
        return StorageOptions.newBuilder();
    }
}
