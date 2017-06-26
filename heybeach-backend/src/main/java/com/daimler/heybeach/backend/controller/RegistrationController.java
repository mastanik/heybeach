package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.RegistrationDto;
import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.exception.RegistrationException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PutMapping
    public ResponseEntity<Response> register(@RequestBody RegistrationDto dto) throws RegistrationException, ValidationException {
        registrationService.register(dto);
        return new ResponseEntity<>(new Response(true, HttpStatus.OK.value()), HttpStatus.OK);
    }
}
