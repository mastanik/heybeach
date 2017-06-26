package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.entities.Role;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.RoleException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/roles")
@Secured(value = "ROLE_AD")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PutMapping
    public ResponseEntity<Response> create(@RequestBody Role role) throws RoleException, ValidationException {
        roleService.create(role);
        return new ResponseEntity<>(new Response(true), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> remove(@PathVariable(value = "id") Long id) throws NotFoundException, RoleException, ValidationException {
        roleService.remove(id);
        return new ResponseEntity<>(new Response(true), HttpStatus.OK);
    }
}
