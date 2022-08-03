package com.jos.dem.vetlog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("jmailer")
public class ApplicationProperties {
    private String url;
}
