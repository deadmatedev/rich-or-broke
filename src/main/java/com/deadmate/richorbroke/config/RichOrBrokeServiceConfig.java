package com.deadmate.richorbroke.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "service.config.richorbroke")
public class RichOrBrokeServiceConfig {
    private String baseCurrencyCode;
    private String upTrendQuery;
    private String downTrendQuery;
    private String zeroTrendQuery;
    private String helpUrl;
}
