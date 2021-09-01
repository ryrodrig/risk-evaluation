package com.cfgtest.services.riskevaluation.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("EnableOpenFeignClient")
@Configuration
public class OpenFeignClientsConfig {

 @Bean
 public Logger.Level feignLoggerLevel() {
     return Logger.Level.FULL;
 }

}
