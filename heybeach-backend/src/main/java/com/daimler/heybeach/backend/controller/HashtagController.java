package com.daimler.heybeach.backend.controller;

import com.daimler.heybeach.backend.dto.Response;
import com.daimler.heybeach.backend.service.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/hashtags")
public class HashtagController {

    @Autowired
    private HashtagService hashtagService;

    @Secured(value = "ROLE_PSU")
    @PostMapping(value = "/autocompletion/{term}")
    public ResponseEntity<Response> autocomplete(@PathVariable(value = "term") String term) {
        List<String> suggestions = hashtagService.suggest(term);
        return new ResponseEntity<>(new Response(true, suggestions, HttpStatus.OK.value()), HttpStatus.OK);
    }
}
