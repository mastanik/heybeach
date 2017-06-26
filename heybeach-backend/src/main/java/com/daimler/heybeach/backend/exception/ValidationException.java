package com.daimler.heybeach.backend.exception;

public class ValidationException extends Exception {

    private Integer code;

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return code;
    }
}
