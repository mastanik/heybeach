package com.daimler.heybeach.backend.exception;

public class PaymentMethodNotSupportedException extends Exception {
    public PaymentMethodNotSupportedException() {
    }

    public PaymentMethodNotSupportedException(String message) {
        super(message);
    }
}
