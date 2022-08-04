package com.jos.dem.vetlog.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApplicationProperties.class)
public class ClientConfiguration {

    private final ApplicationProperties applicationProperties;
    private final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

    Interceptor interceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder().addHeader("Content-Type", "application/json ").build();
            return chain.proceed(request);
        }
    };

    @Bean
    public Retrofit retrofit() {
        log.info("Calling service with url: {}", applicationProperties.getUrl());
        okHttpClient.interceptors().add(interceptor);
        return new Retrofit.Builder()
                .baseUrl(applicationProperties.getUrl())
                .client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
