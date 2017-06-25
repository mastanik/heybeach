package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.PictureUploadDto;
import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.exception.PictureException;
import com.daimler.heybeach.backend.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Secured(value = "ROLE_PSU")
    @PostMapping(value = "/upload")
    public ResponseEntity<Response> upload(PictureUploadDto pictureUpload, @RequestParam("picture") MultipartFile file) {
        Response response = new Response();
        try {
            pictureService.save(pictureUpload, file);
            response.setSuccess(true);
        } catch (PictureException e) {
            response.setErrorMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured(value = "ROLE_AD")
    @PostMapping(value = "/{id}/approve")
    public ResponseEntity<Response> approve(@PathVariable(value = "id") Long id) {
        Response response = new Response();
        try {
            pictureService.approve(id);
        } catch (PictureException e) {
            response.setErrorMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured(value = {"ROLE_PSU", "ROLE_PBU"})
    @PostMapping(value = "/{id}/like")
    public ResponseEntity<Response> like(@PathVariable(value = "id") Long id) {
        Response response = new Response();
        try {
            pictureService.like(id);
        } catch (PictureException e) {
            response.setErrorMessage(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
