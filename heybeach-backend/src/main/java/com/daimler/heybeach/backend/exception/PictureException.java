package com.daimler.heybeach.backend.exception;

public class PictureException extends ServiceException {
    public PictureException() {
    }

    public PictureException(String message) {
        super(message);
    }

    public PictureException(String message, Throwable cause) {
        super(message, cause);
    }
}
