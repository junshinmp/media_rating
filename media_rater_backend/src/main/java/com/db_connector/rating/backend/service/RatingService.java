package com.db_connector.rating.backend.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.db_connector.rating.backend.model.Rating;
import com.db_connector.rating.backend.repositories.RatingRepository;

import jakarta.transaction.Transactional;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final TMDBService tmdbService;

    public RatingService(RatingRepository ratingRepository, UserService userService, TMDBService tmdbService){
        this.ratingRepository = ratingRepository;
        this.userService = userService;
        this.tmdbService = tmdbService;
    }

    /**
     * =============================== FIND STATEMENTS ===============================
     */

    public Rating findByRatingId(int ratingId){
        Rating currRating = ratingRepository.findById(ratingId).orElseThrow(
            () -> new IllegalArgumentException("Rating: " + ratingId + " does not exist")
        );

        return currRating;
    }

    public List<Rating> findByUserId(int userId){
        if (!userService.userExists(userId)){
            throw new IllegalArgumentException("User does not exist.");
        }

        return ratingRepository.findByUserId(userId);
    }

    public List<Rating> findByMediaId(String mediaId){
        if (mediaId == null || mediaId.trim().isEmpty()) {
            throw new IllegalArgumentException("Media ID cannot be empty.");
        }

        if (!tmdbService.mediaExists(mediaId)){
            throw new IllegalArgumentException("Media ID: " + mediaId + " is not valid.");
        }

        return ratingRepository.findByMediaId(mediaId);
    }

    public Rating existsByMediaIdAndUserId(String mediaId, int userId){
        if (mediaId == null || mediaId.trim().isEmpty()) {
            throw new IllegalArgumentException("Media ID cannot be empty.");
        }
        return ratingRepository.findByUserIdAndMediaId(userId, mediaId)
            .orElseThrow(() -> new IllegalArgumentException("No rating found for this user and media."));
    }

    public Double getAverageRatingForMedia(String mediaId) {
        if (mediaId == null || mediaId.trim().isEmpty()) {
            throw new IllegalArgumentException("Media ID cannot be empty.");
        }
        Double avg = ratingRepository.findAverageStarsByMediaId(mediaId);
        return (avg != null) ? avg : 0.0;
    }

    /**
     * ======================== TRANSACTIONAL STATEMENTS ========================
     */

    @Transactional
    public void deleteRating(int ratingId) {
        if (!ratingRepository.existsById(ratingId)) {
            throw new IllegalArgumentException("Rating with ID " + ratingId + " does not exist.");
        }
        ratingRepository.deleteById(ratingId);
    }

    @Transactional
    public void deleteByUserId(int userId){
        if (!userService.userExists(userId)){
            throw new IllegalArgumentException("User does not exist.");
        }
        
        ratingRepository.deleteByUserId(userId);
    }
    
    @Transactional
    public Rating changeRating(int ratingId, int stars, String comments){
        Rating currRating = ratingRepository.findById(ratingId).orElseThrow(
            () -> new IllegalArgumentException("Rating " + ratingId + " does not exist.")
        );

        if (stars <= 0 || stars > 5){
            throw new IllegalArgumentException("Stars must be between 1 - 5.");
        }
        
        currRating.setStars(stars);
        currRating.setComments(comments);

        return ratingRepository.save(currRating);
    }

    @Transactional
    public Rating createRating(Rating newRating){
        if (!userService.userExists(newRating.getUserId())) {
            throw new IllegalArgumentException("User does not exist.");
        }

        if (!tmdbService.mediaExists(newRating.getMediaId())){
            throw new IllegalArgumentException("Media id does not exist.");
        }
        
        if (newRating.getStars() <= 0 || newRating.getStars() > 5){
            throw new IllegalArgumentException("Rating Stars must be between 1 - 5.");
        }

        if (newRating.getComments() != null) {
            newRating.setComments(newRating.getComments().trim());
        }

        boolean alreadyExists = ratingRepository.existsByUserIdAndMediaId(newRating.getUserId(), newRating.getMediaId());
        if (alreadyExists) {
            throw new IllegalStateException("User has already rated this media. Use update instead.");
        }

        return ratingRepository.save(newRating);
    }
}