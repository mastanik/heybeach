package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.entities.Order;
import org.springframework.stereotype.Service;

@Service
public class VisaPaymentProvider implements PaymentProvider {
    @Override
    public PaymentResult process(Order order) {
        PaymentResult result = new PaymentResult();
        result.setSuccess(true);
        result.setTransactionId("Visa-transaction-id");
        return result;
    }
}
