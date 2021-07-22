package com.cfgtest.services.riskevaluation.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "customer-service")
@Data
public class CustomerServiceSettings {

    private String url;

}
