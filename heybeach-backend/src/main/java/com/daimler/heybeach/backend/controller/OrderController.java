package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.OrderDto;
import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.OrderException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Secured(value = "ROLE_PBU")
    @PutMapping
    public ResponseEntity<Response> purchase(@RequestBody OrderDto dto) throws NotFoundException, OrderException, ValidationException {
        orderService.purchase(dto);
        return new ResponseEntity<>(new Response(true, HttpStatus.OK.value()), HttpStatus.OK);
    }

}
