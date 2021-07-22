package com.cfgtest.services.riskevaluation.repository;

import com.cfgtest.services.riskevaluation.dto.RiskModulePayload;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

// Provides paging functionality.
public interface RiskModulePayloadRepository extends PagingAndSortingRepository<RiskModulePayload, UUID> {
}
