package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.dto.UserDto;
import com.daimler.heybeach.backend.entities.User;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.UserException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@Secured(value = "ROLE_AD")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users/{id}")
    public ResponseEntity getUser(@PathVariable(value = "id") Long id) throws NotFoundException, UserException, ValidationException {
        User user = userService.findById(id);
        return new ResponseEntity<>(new Response<>(true, user), HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity getUsers(Principal principal) throws UserException, ValidationException {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(new Response<>(true, users), HttpStatus.OK);
    }

    @PostMapping(value = "/users/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody UserDto user) throws NotFoundException, UserException, ValidationException {
        userService.update(id, user);
        return new ResponseEntity<>(new Response<>(true), HttpStatus.OK);
    }

    @PutMapping(value = "/users")
    public ResponseEntity create(@RequestBody User user) throws UserException, ValidationException {
        userService.create(user);
        return new ResponseEntity<>(new Response<>(true), HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity remove(@PathVariable(value = "id") Long id) throws NotFoundException, UserException, ValidationException {
        userService.remove(id);
        return new ResponseEntity<>(new Response<>(true), HttpStatus.OK);

    }
}
