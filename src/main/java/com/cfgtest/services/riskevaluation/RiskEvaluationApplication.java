package com.cfgtest.services.riskevaluation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// annotation to enable openApIDefinition that in turn generates swagger documentation.
// Open API is a specification and swagger has a list of tools for implementing these specification.
@OpenAPIDefinition
@SpringBootApplication
// Enables Feign clients.. Spring takes care of boiler plate code of making client calls.
@EnableFeignClients
public class RiskEvaluationApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiskEvaluationApplication.class, args);
	}

}
