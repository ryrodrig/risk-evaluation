package com.cfgtest.services.riskevaluation;

import com.cfgtest.services.riskevaluation.model.RiskCheckResponse;
import com.cfgtest.services.riskevaluation.service.RiskCheckService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

// Usually used to perform Integration testing.

// Useful when we need to bootstrap the entire spring container.
// Works by creating application context
@SpringBootTest(classes = RiskEvaluationApplication.class)
@TestPropertySource(locations="classpath:application-integration.properties")
class RiskEvaluationApplicationTests {

	@Autowired
	private RiskCheckService riskCheckService;

	@Test
	void contextLoads() {
	}

	@Test
	public void integrationTestRiskCheck() {
		RiskCheckResponse riskCheckResponse = riskCheckService.performRiskCheck("1234");
		Assertions.assertNotNull(riskCheckResponse);
		Assertions.assertEquals(riskCheckResponse.getStatus(), RiskCheckResponse.StatusEnum.APPROVED);
	}


}
