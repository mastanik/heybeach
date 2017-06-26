package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.PaymentDto;
import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.OrderException;
import com.daimler.heybeach.backend.exception.PaymentException;
import com.daimler.heybeach.backend.exception.PaymentFailedException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Secured(value = "ROLE_PBU")
    @PutMapping(value = "/{orderId}")
    public ResponseEntity<Response> pay(@PathVariable("orderId") Long orderId, @RequestBody PaymentDto paymentDto) throws NotFoundException, OrderException, ValidationException, PaymentException, PaymentFailedException {
        paymentService.makePayment(orderId, paymentDto);
        return new ResponseEntity<>(new Response(true), HttpStatus.OK);
    }
}
