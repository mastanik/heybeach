package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.dto.UserDto;
import com.daimler.heybeach.backend.entities.User;
import com.daimler.heybeach.backend.exception.UserException;
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
    public ResponseEntity getUser(@PathVariable(value = "id") Long id) {
        User user;
        Response<User> response;
        try {
            user = userService.findById(id);
            response = new Response<>(true, user);

        } catch (UserException e) {
            response = new Response<>(false, e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity getUsers(Principal principal) {
        List<User> users;
        Response<List<User>> response;
        try {
            users = userService.findAll();
            response = new Response<>(true, users);
        } catch (UserException e) {
            response = new Response<>(false, e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/users/{id}")
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody UserDto user) {
        Response response;
        try {
            userService.update(id, user);
            response = new Response(true);
        } catch (UserException e) {
            response = new Response(false, e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(value = "/users")
    public ResponseEntity create(@RequestBody User user) {
        Response response;
        try {
            userService.create(user);
            response = new Response(true);
        } catch (UserException e) {
            response = new Response(false, e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity remove(@PathVariable(value = "id") Long id) {
        Response response;
        try {
            userService.remove(id);
            response = new Response(true);
        } catch (UserException e) {
            response = new Response(false, e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
