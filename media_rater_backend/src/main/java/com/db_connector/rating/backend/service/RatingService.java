package com.db_connector.rating.backend.service;

import org.springframework.stereotype.Service;
import com.db_connector.rating.backend.repositories.RatingRepository;
import com.db_connector.rating.backend.model.Rating;
import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserService userService;

    public RatingService(RatingRepository ratingRepository, UserService userService){
        this.ratingRepository = ratingRepository;
        this.userService = userService;
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
        return ratingRepository.findByMediaId(mediaId);
    }

    public void deleteRating(int ratingId) {
        if (!ratingRepository.existsById(ratingId)) {
            throw new IllegalArgumentException("Rating with ID " + ratingId + " does not exist.");
        }
        ratingRepository.deleteById(ratingId);
    }

    public void deleteByUserid(int userId){
        if (!userService.userExists(userId)){
            throw new IllegalArgumentException("User does not exist.");
        }
        
        ratingRepository.deleteByUserId(userId);
    }
}
