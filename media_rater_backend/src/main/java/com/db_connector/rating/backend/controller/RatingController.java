package com.db_connector.rating.backend.controller;

import com.db_connector.rating.backend.service.RatingService;
import com.db_connector.rating.backend.model.Rating;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService){
        this.ratingService = ratingService;
    }

    /**
     * =============================== FIND STATEMENTS ===============================
     */

    @GetMapping("/ratingId")
    public ResponseEntity<?> findByRatingId(@RequestParam int ratingId){
        try {
            Rating currRating = ratingService.findByRatingId(ratingId);
            return ResponseEntity.ok(currRating);
        } catch (RuntimeException re){
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    @GetMapping("/ratingUserId")
    public ResponseEntity<?> findByUserId(@RequestParam int userId){
        try {
            List<Rating> ratings = ratingService.findByUserId(userId);
            return ResponseEntity.ok(ratings);
        } catch (RuntimeException re){
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    @GetMapping("/ratingsMediaId")
    public ResponseEntity<?> findByMediaId(@RequestParam String mediaId){
        try {
            List<Rating> ratings = ratingService.findByMediaId(mediaId);
            return ResponseEntity.ok(ratings);
        } catch (RuntimeException re){
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    @GetMapping("/ratingUserAndMedia")
    public ResponseEntity<?> existsByMediaIdAndUserId(@RequestParam String mediaId, 
            @RequestParam int userId){
        try {
            Rating currRating = ratingService.existsByMediaIdAndUserId(mediaId, userId);
            return ResponseEntity.ok(currRating);
        } catch (RuntimeException re){
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    @GetMapping("/averageRating")
    public ResponseEntity<?> getAverageRatingForMedia(@RequestParam String mediaId){
        try {
            Double currRating = ratingService.getAverageRatingForMedia(mediaId);
            return ResponseEntity.ok(currRating);
        } catch (RuntimeException re){
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    /**
     * ======================== TRANSACTIONAL STATEMENTS ========================
     */
    @DeleteMapping("/deleteRating")
    public ResponseEntity<?> deleteRating(@RequestParam int ratingId){
        try {
            ratingService.deleteRating(ratingId);
            return ResponseEntity.ok("Rating " + ratingId + " successfully deleted.");
        } catch (RuntimeException re){
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    @DeleteMapping("/deleteRatingByUser")
    public ResponseEntity<?> deleteByUserId(@RequestParam int userId){
        try {
            ratingService.deleteByUserId(userId);
            return ResponseEntity.ok("Ratings from user " + userId + " successfully deleted.");
        } catch (RuntimeException re){
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    @PostMapping("/createRating")
    public ResponseEntity<?> createRating(@RequestBody Rating newRating){
        try {
            Rating created = ratingService.createRating(newRating);
            return ResponseEntity.ok(created);
        } catch (RuntimeException re){
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    @PutMapping("/changeRating")
    public ResponseEntity<?> changeRating(@RequestParam int ratingId, 
        @RequestParam int stars, @RequestParam String comments
    ){
        try {
            Rating updated = ratingService.changeRating(ratingId, stars, comments);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException re){
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }
}