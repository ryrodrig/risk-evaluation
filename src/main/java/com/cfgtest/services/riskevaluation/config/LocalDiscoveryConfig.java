package com.cfgtest.services.riskevaluation.config;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

// Enable client service as a eureka client... By default it will try to find a server on
// localhost for registration.
@EnableEurekaClient
@Configuration
public class LocalDiscoveryConfig {
}
