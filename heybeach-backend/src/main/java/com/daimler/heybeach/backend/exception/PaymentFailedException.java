package com.daimler.heybeach.backend.exception;

public class PaymentFailedException extends Exception{
    public PaymentFailedException() {
    }

    public PaymentFailedException(String message) {
        super(message);
    }
}
