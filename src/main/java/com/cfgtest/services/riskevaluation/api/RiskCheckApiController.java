package com.cfgtest.services.riskevaluation.api;

import com.cfgtest.services.riskevaluation.model.RiskCheckResponse;
import com.cfgtest.services.riskevaluation.service.RiskCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RiskCheckApiController implements RiskCheckApi{

    private final RiskCheckService riskCheckService;


    @Override
    public ResponseEntity<RiskCheckResponse> riskCheck(String transactionId, String xCorrelationId) {
        log.info("Risk Check implementation...");
        return ResponseEntity.ok(riskCheckService.performRiskCheck(transactionId));
    }

}
