package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dao.BeachDao;
import com.daimler.heybeach.backend.entities.Beach;
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
public class BeachController {

    @Autowired
    private BeachDao beachDao;

    @RequestMapping(value = "/beaches/{id}", method = RequestMethod.GET)
    public ResponseEntity getUser(@PathVariable(value = "id") Long id) {
        Beach beach = null;
        try {
            beach = beachDao.findById(id);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(beach, HttpStatus.OK);
    }

    @RequestMapping(value = "/beaches", method = RequestMethod.GET)
    public ResponseEntity getUsers() {
        List<Beach> beach = null;
        try {
            beach = beachDao.findAll();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(beach, HttpStatus.OK);
    }

    @RequestMapping(value = "/beaches/{id}", method = RequestMethod.POST)
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody Beach beach) {
        try {
            beach.setId(id);
            beachDao.save(beach);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(beach, HttpStatus.OK);
    }

    @RequestMapping(value = "/beaches", method = RequestMethod.PUT)
    public ResponseEntity create(@RequestBody Beach beach) {
        try {
            beachDao.save(beach);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(beach, HttpStatus.OK);
    }

    @RequestMapping(value = "/beaches/{id}", method = RequestMethod.DELETE)
    public ResponseEntity remove(@PathVariable(value = "id") Long id) {
        Beach beach = null;
        try {
            beach = beachDao.findById(id);
            beachDao.remove(beach);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(beach, HttpStatus.OK);
    }
}
