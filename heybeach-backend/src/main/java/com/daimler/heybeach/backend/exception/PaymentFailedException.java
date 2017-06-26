package com.daimler.heybeach.backend.exception;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException() {
    }

    public PaymentFailedException(String message) {
        super(message);
    }
}
