package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.ApproveDto;
import com.daimler.heybeach.backend.dto.PictureUploadDto;
import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.exception.NotFoundException;
import com.daimler.heybeach.backend.exception.PictureException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.backend.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/picture")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Secured(value = "ROLE_PSU")
    @PostMapping(value = "/upload")
    public ResponseEntity<Response> upload(PictureUploadDto pictureUpload, @RequestParam("picture") MultipartFile file) throws PictureException, ValidationException {
        pictureService.save(pictureUpload, file);
        return new ResponseEntity<>(new Response(true, HttpStatus.OK.value()), HttpStatus.OK);
    }

    @Secured(value = "ROLE_PSU")
    @DeleteMapping(value = "/{id}/remove")
    public ResponseEntity<Response> remove(@PathVariable(value = "id") Long id) throws PictureException, NotFoundException, ValidationException {
        pictureService.remove(id);
        return new ResponseEntity<>(new Response(true, HttpStatus.OK.value()), HttpStatus.OK);
    }

    @Secured(value = "ROLE_AD")
    @PostMapping(value = "/{id}/approve")
    public ResponseEntity<Response> approve(@PathVariable(value = "id") Long id, @RequestBody ApproveDto dto) throws NotFoundException, PictureException, ValidationException {
        pictureService.approve(id, dto);
        return new ResponseEntity<>(new Response(true, HttpStatus.OK.value()), HttpStatus.OK);
    }

    @Secured(value = "ROLE_AD")
    @PostMapping(value = "/{id}/disapprove")
    public ResponseEntity<Response> disapprove(@PathVariable(value = "id") Long id, @RequestBody ApproveDto dto) throws NotFoundException, PictureException, ValidationException {
        pictureService.disapprove(id, dto);
        return new ResponseEntity<>(new Response(true, HttpStatus.OK.value()), HttpStatus.OK);

    }

    @Secured(value = {"ROLE_PSU", "ROLE_PBU"})
    @PostMapping(value = "/{id}/like")
    public ResponseEntity<Response> like(@PathVariable(value = "id") Long id) throws NotFoundException, PictureException, ValidationException {
        pictureService.like(id);
        return new ResponseEntity<>(new Response(true, HttpStatus.OK.value()), HttpStatus.OK);
    }
}
