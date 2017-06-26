package com.daimler.heybeach.backend.exception;

public class PaymentException extends ServiceException {
    public PaymentException() {
    }

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
