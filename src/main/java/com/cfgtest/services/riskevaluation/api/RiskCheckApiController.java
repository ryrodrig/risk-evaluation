package com.cfgtest.services.riskevaluation.api;

import com.cfgtest.services.riskevaluation.model.RiskCheckResponse;
import com.cfgtest.services.riskevaluation.model.RiskCheckResponseLink;
import com.cfgtest.services.riskevaluation.service.RiskCheckService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Profile("!reactive-enable")
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

    @RequestMapping(value = "/riskcheck-failover", produces = "application/json", method =
            RequestMethod.GET)
    public ResponseEntity<RiskCheckResponse> riskCheckFailover() {
        log.info("Risk Check Failover implementation...");
        return ResponseEntity.ok(new RiskCheckResponse().description("Failover Response").status(RiskCheckResponse.StatusEnum.PENDED).addLinkItem(new RiskCheckResponseLink()));
    }

//    @RequestMapping(value = "/asyncRiskCheck/{transactionId}",
//            produces = { "application/json" },
//            method = RequestMethod.GET)
//    public Mono<ResponseEntity<RiskCheckResponse>> asyncRiskCheck(String transactionId,
//                                                             String xCorrelationId) {
//        log.info("Risk Check async implementation...");
//        Mono<ResponseEntity<RiskCheckResponse>> responseEntityMono = riskCheckService.performAsyncRiskCheck(transactionId).flatMap(riskCheckResponse -> {
//            log.info("Risk Check Controller..");
//            return Mono.fromSupplier(() -> ResponseEntity.ok(riskCheckResponse));
//        });
//        // logged before mono returns.. since the controller returns Mono responsibility of the
//        // server to handle returning response to client in a synch manner.
//        log.info("Perform Async Risk Check invoked");
//        return responseEntityMono;
//    }


    @RequestMapping(value = "/testRiskCheck/{transactionId}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<RiskCheckResponse> riskCheckTest(String transactionId,
                                                                  String xCorrelationId) {
        log.info("Risk Check implementation...");
        return ResponseEntity.ok(riskCheckService.performRiskCheck(transactionId));
    }

}
