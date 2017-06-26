package com.daimler.heybeach.backend.exception;

public class OrderException extends ServiceException {
    public OrderException() {
    }

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
