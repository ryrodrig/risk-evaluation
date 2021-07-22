package com.cfgtest.services.riskevaluation.service;

import com.cfgtest.services.riskevaluation.model.RiskCheckResponse;

public interface RiskCheckService {

    RiskCheckResponse performRiskCheck(String transactionId);
}
