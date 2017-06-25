package com.daimler.heybeach.backend.exception;

public class PictureStorageException extends Exception {
    public PictureStorageException() {
    }

    public PictureStorageException(String message) {
        super(message);
    }

    public PictureStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public PictureStorageException(Throwable cause) {
        super(cause);
    }
}
