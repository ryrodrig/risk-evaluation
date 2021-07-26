package com.cfgtest.services.riskevaluation.repository;

import com.cfgtest.services.riskevaluation.dto.RiskTransactionDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;
// Should be an interface .. implementation would be provided by spring.
public interface RiskTransactionDetailsRepository extends CrudRepository<RiskTransactionDetails,UUID> {
}
