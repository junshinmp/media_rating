package com.db_connector.rating.backend.controller;

import com.db_connector.rating.backend.service.TMDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class TMDBController {
    private final TMDBService tmdbService;

    public TMDBController(TMDBService tmdbService){
        this.tmdbService = tmdbService;
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchMedia(@RequestParam String title){
        String mediaResponse = tmdbService.searchMedia(title);

        return ResponseEntity.ok(mediaResponse);
    }

    @GetMapping("/movieFromId")
    public ResponseEntity<String> mediaFromId(@RequestParam String id){
        String mediaResponse = tmdbService.mediaFromId(id);

        return ResponseEntity.ok(mediaResponse);
    }
}