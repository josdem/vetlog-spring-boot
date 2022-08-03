package com.jos.dem.vetlog.config;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApplicationProperties.class)
public class ClientConfiguration {

    private final ApplicationProperties applicationProperties;
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    @Bean
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(applicationProperties.getUrl())
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }
}
