package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.exception.PaymentMethodNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentProviderLocator {

    @Autowired
    private PaypalPaymentProvider paypalPaymentProvider;

    @Autowired
    private VisaPaymentProvider visaPaymentProvider;

    @Autowired
    private MastercardPaymentProvider mastercardPaymentProvider;

    public PaymentProvider locate(String name) throws PaymentMethodNotSupportedException {
        if (PaymentMethods.PAYPAL.getPaymentName().equals(name)) {
            return paypalPaymentProvider;
        } else if (PaymentMethods.VISA.getPaymentName().equals(name)) {
            return visaPaymentProvider;
        } else if (PaymentMethods.MASTERCARD.getPaymentName().equals(name)) {
            return mastercardPaymentProvider;
        }
        throw new PaymentMethodNotSupportedException("Payment method not supported");
    }
}
