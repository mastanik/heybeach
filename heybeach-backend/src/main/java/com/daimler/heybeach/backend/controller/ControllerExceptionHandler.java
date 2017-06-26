package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.PaymentFailedException;
import com.daimler.heybeach.backend.exception.ServiceException;
import com.daimler.heybeach.backend.exception.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new Response(false, ex.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<Object> handleService(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new Response(false, "Internal error occured"),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidation(ValidationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (ex.getCode() != null && ex.getCode() != 0) {
            status = HttpStatus.CONFLICT;
        }
        return handleExceptionInternal(ex, new Response(false, ex.getMessage()),
                new HttpHeaders(), status, request);
    }

    @ExceptionHandler(value = {PaymentFailedException.class})
    protected ResponseEntity<Object> handlePayment(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new Response(false, ex.getMessage()),
                new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
