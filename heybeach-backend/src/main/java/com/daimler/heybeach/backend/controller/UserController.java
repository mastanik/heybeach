package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dao.UserDao;
import com.daimler.heybeach.backend.entities.User;
import com.daimler.heybeach.backend.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable(value = "id") Long id) {
        User user = null;
        try {
            user = userDao.findById(id);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity getUsers() {
        List<User> user = null;
        try {
            user = userDao.findAll();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody User user) {
        try {
            user.setId(id);
            userDao.save(user);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    public ResponseEntity create(@RequestBody User user) {
        try {
            userDao.save(user);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity remove(@PathVariable(value = "id") Long id) {
        User user = null;
        try {
            user = userDao.findById(id);
            userDao.remove(user);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
