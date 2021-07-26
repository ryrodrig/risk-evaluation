package com.cfgtest.services.riskevaluation.service;

import com.cfgtest.services.riskevaluation.model.RiskCheckResponse;
import reactor.core.publisher.Mono;

public interface RiskCheckService {

    RiskCheckResponse performRiskCheck(String transactionId);

    Mono<RiskCheckResponse> performAsyncRiskCheck(String transactionId);
}
