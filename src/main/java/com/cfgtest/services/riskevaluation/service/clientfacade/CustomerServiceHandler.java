package com.cfgtest.services.riskevaluation.service.clientfacade;

import com.cfgtest.client.customerservice.model.Customer;

public interface CustomerServiceHandler {

    Customer getCustomerById(String customerId);

}
