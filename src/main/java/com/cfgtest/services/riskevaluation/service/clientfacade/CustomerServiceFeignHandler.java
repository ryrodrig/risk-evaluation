package com.cfgtest.services.riskevaluation.service.clientfacade;

import com.cfgtest.client.customerservice.model.Customer;
import com.cfgtest.services.riskevaluation.config.OpenFeignClientsConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Profile("EnableOpenFeignClient")
@FeignClient(name="cfg-service-gateway", configuration = OpenFeignClientsConfig.class)
@Service
public interface CustomerServiceFeignHandler extends CustomerServiceHandler {

    @GetMapping(path = "${customer.get-customer-id.path}", produces = "application/json" , name=
            "getCustomerById")
    Customer getCustomerById(@PathVariable("customerId") String customerId);

}
