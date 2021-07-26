package com.cfgtest.services.riskevaluation.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.cfgtest.services.riskevaluation.dto.RiskModulePayload;
import com.cfgtest.services.riskevaluation.dto.RiskTransactionDetails;
import com.cfgtest.services.riskevaluation.model.RiskCheckResponse;
import com.cfgtest.services.riskevaluation.repository.RiskModulePayloadRepository;
import com.cfgtest.services.riskevaluation.repository.RiskTransactionDetailsRepository;
import com.cfgtest.services.riskevaluation.settings.CustomerServiceSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnlineRiskCheckService implements RiskCheckService {

    private final RiskTransactionDetailsRepository riskTransactionDetailsRepository;

    private final RiskModulePayloadRepository riskModulePayloadRepository;

    private final WebClient webClient;

    private final CustomerServiceSettings customerServiceSettings;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public RiskCheckResponse performRiskCheck(String transactionId) {
        // Retrieve customer information from customer service micro service
        // Invoke Risk Check with customer retrieved information

        String customerDetails = webClient.get().uri(customerServiceSettings.getUrl() + "123").retrieve().bodyToMono(String.class).block();
        // Store risk check payload into database
        RiskModulePayload persistedRiskModulePayload =
                riskModulePayloadRepository.save(createRiskModulePayload("N/A",customerDetails).get());
//        RiskModulePayload persistedRiskModulePayload =
//                riskModulePayloadRepository.save(createStubRiskModulePayload().get());
        log.info("RiskModulePayload : {} ", persistedRiskModulePayload);
        return new RiskCheckResponse().status(mapStatusEnum(persistedRiskModulePayload)).description("Customer Approved");
    }

    @Override
    public Mono<RiskCheckResponse> performAsyncRiskCheck(String transactionId) {
        // Retrieve customer information from customer service micro service
        // Invoke Risk Check with customer retrieved information
        Mono<String> customerServiceMono =
                webClient.get().uri(customerServiceSettings.getUrl() + "123").retrieve().bodyToMono(String.class);
        log.info("Service call complete..." + customerServiceMono.log());
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // Subscribe is an async call and the callback is invoked once the producer completes
        // execution
//        customerServiceMono.subscribe((str) -> {
//            log.info("Subscribe" + str);
//        });
        return customerServiceMono.flatMap((customerDetails) -> {
            log.info("From FlatMap");
            RiskModulePayload persistedRiskModulePayload =
                    riskModulePayloadRepository.save(createRiskModulePayload("N/A",customerDetails).get());
            return Mono.fromSupplier(() -> new RiskCheckResponse().status(mapStatusEnum(persistedRiskModulePayload)).description("Customer Approved Async"));
        });

    }

    private RiskCheckResponse.StatusEnum mapStatusEnum(RiskModulePayload riskModulePayload) {
        return RiskCheckResponse.StatusEnum.valueOf(riskModulePayload.getRiskTransactionDetails().getRiskStatus().name());
    }


    private Supplier<RiskModulePayload> createStubRiskModulePayload() {
        return () -> {
            RiskTransactionDetails riskTransactionDetails = riskTransactionDetailsRepository.save(createRiskTransactionDetailsSupplier().get());
            return RiskModulePayload.builder().riskTransactionDetails(riskTransactionDetails).requestPayload("Stub Request Payload").responsePayload("Stub Response Payload").deleteIndicator(false).build();
        };
    }

    private Supplier<RiskModulePayload> createRiskModulePayload(String request, String response) {
        return () -> {
            RiskTransactionDetails riskTransactionDetails = riskTransactionDetailsRepository.save(createRiskTransactionDetailsSupplier().get());
            return RiskModulePayload.builder().riskTransactionDetails(riskTransactionDetails).requestPayload(request).responsePayload(response).deleteIndicator(false).build();
        };
    }


    private Supplier<RiskTransactionDetails> createRiskTransactionDetailsSupplier() {
        return () -> {
            return RiskTransactionDetails.builder().riskStatus(RiskTransactionDetails.RiskStatus.APPROVED).deleteIndicator(false).build();
        };
    }
}
