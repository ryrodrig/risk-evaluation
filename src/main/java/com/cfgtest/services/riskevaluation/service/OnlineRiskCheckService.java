package com.cfgtest.services.riskevaluation.service;

import com.cfgtest.client.customerservice.model.Customer;
import com.cfgtest.client.customerservice.model.ErrorDetails;
import com.cfgtest.services.riskevaluation.dto.RiskModulePayload;
import com.cfgtest.services.riskevaluation.dto.RiskTransactionDetails;
import com.cfgtest.services.riskevaluation.exceptionhandlers.RiskEvaluationServiceException;
import com.cfgtest.services.riskevaluation.model.RiskCheckResponse;
import com.cfgtest.services.riskevaluation.repository.RiskModulePayloadRepository;
import com.cfgtest.services.riskevaluation.repository.RiskTransactionDetailsRepository;
import com.cfgtest.services.riskevaluation.service.clientfacade.CustomerServiceHandler;
import com.cfgtest.services.riskevaluation.settings.CustomerServiceSettings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnlineRiskCheckService implements RiskCheckService {

    private final RiskTransactionDetailsRepository riskTransactionDetailsRepository;

    private final RiskModulePayloadRepository riskModulePayloadRepository;

    private final CustomerServiceHandler customerServiceHandler;

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public RiskCheckResponse performRiskCheck(String transactionId) {
        // Retrieve customer information from customer service micro service
        // Invoke Risk Check with customer retrieved information

        Customer customerDetails = customerServiceHandler.getCustomerById(transactionId);
        Integer isEligible = Optional.ofNullable(customerDetails.getDateOfBirth()).map(dob -> Period.between(dob,
                LocalDate.now()).getYears()).filter(age -> age > 18).orElse(-1);


        if(isEligible < 0) {
            return  new RiskCheckResponse().status(RiskCheckResponse.StatusEnum.PENDED).description("Customer age eligibility criteria failed. Should be above the age of 18 years.");
        }
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        try {
            String customerJsonStr = objectMapper.writeValueAsString(customerDetails);
            RiskModulePayload persistedRiskModulePayload =
                    riskModulePayloadRepository.save(createRiskModulePayload("N/A",customerJsonStr).get());
            log.info("RiskModulePayload : {} ", persistedRiskModulePayload);
            return new RiskCheckResponse().status(mapStatusEnum(persistedRiskModulePayload)).description("Customer Approved");
        } catch (JsonProcessingException e) {
//            String message, ErrorDetails.ResultEnum resultEnum, String sourceSystem, String traceId, Integer errorCode
            throw new RiskEvaluationServiceException(e.getMessage(),
                    ErrorDetails.ResultEnum.FAILED,"JSON PARSING", null,
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        // Store risk check payload into database

    }

//    @Override
//    public Mono<RiskCheckResponse> performAsyncRiskCheck(String transactionId) {
//        // Retrieve customer information from customer service micro service
//        // Invoke Risk Check with customer retrieved information
//        Mono<String> customerServiceMono =
//                webClient.get().uri(customerServiceSettings.getUrl() + "123").retrieve().bodyToMono(String.class);
//        log.info("Service call complete..." + customerServiceMono.log());
////        try {
////            Thread.sleep(5000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        // Subscribe is an async call and the callback is invoked once the producer completes
//        // execution
////        customerServiceMono.subscribe((str) -> {
////            log.info("Subscribe" + str);
////        });
//        return customerServiceMono.flatMap((customerDetails) -> {
//            log.info("From FlatMap");
//            RiskModulePayload persistedRiskModulePayload =
//                    riskModulePayloadRepository.save(createRiskModulePayload("N/A",customerDetails).get());
//            return Mono.fromSupplier(() -> new RiskCheckResponse().status(mapStatusEnum(persistedRiskModulePayload)).description("Customer Approved Async"));
//        });
//
//    }

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
