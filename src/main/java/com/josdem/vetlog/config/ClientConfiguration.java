/*
  Copyright 2026 Jose Morales contact@josdem.io

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

package com.josdem.vetlog.config;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final GmailerProperties gmailerProperties;
    private final GoogleProperties googleProperties;
    private final VetlogBackendProperties vetlogBackendProperties;
    private final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(gmailerProperties.getUrl())
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public Retrofit googleRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(googleProperties.getUrl())
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public Retrofit vetlogRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(vetlogBackendProperties.getUrl())
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
