package com.daimler.heybeach.backend.exception;

public class RoleException extends ServiceException {
    public RoleException() {
    }

    public RoleException(String message) {
        super(message);
    }

    public RoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
