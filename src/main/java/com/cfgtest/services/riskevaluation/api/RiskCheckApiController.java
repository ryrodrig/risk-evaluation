package com.cfgtest.services.riskevaluation.api;

import com.cfgtest.services.riskevaluation.model.RiskCheckResponse;
import com.cfgtest.services.riskevaluation.model.RiskCheckResponseCombinedDecison;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RiskCheckApiController implements RiskCheckApi{

    @Override
    public ResponseEntity<RiskCheckResponse> riskCheck(String transactionId, String channel, String userId, Boolean isExistingCustomer, String xCorrelationId, String applicantIPAddr, String applicantId, Boolean prescreenOnlineCustomer, String bankNumber, String branchCode, String citizensDDACode, String productPath, String env, String requestorAppId, String productIds) {
        log.info("Risk Check implementation...");
        return ResponseEntity.ok(new RiskCheckResponse().combinedDecison(new RiskCheckResponseCombinedDecison().status(RiskCheckResponseCombinedDecison.StatusEnum.APPROVED)));
    }
}
