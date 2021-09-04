package com.deadmate.richorbroke.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "client.config.giphy")
public class GiphyClientConfig {
    private String url;
    private String apiKey;
    private int maxRandomOffset;
    private String imageUrlFormat;
}
