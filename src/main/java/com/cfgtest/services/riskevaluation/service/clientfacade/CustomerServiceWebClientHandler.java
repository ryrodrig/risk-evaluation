package com.cfgtest.services.riskevaluation.service.clientfacade;

import com.cfgtest.client.customerservice.model.Customer;
import com.cfgtest.services.riskevaluation.settings.CustomerServiceSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

// Webclient implementation.
@Profile("!EnableOpenFeignClient")
@Service
@RequiredArgsConstructor
public class CustomerServiceWebClientHandler implements  CustomerServiceHandler{

    private final CustomerServiceSettings customerServiceSettings;

    private final WebClient webClient;

    @Override
    public Customer getCustomerById(String customerId) {
        return
                webClient.get().uri(customerServiceSettings.getUrl() + customerId ).retrieve().bodyToMono(Customer.class).block();
    }
}
