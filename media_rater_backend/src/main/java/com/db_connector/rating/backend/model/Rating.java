package com.db_connector.rating.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ratings")
public class Rating {

    // Rating related data
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private int ratingId;

    // Media related data
    // Will link to the TMDBService and collect the correlating movie related
    // to the review
    // Format: tt000000...
    @Column(name = "media_id")
    private String mediaId;

    // User related data
    private int stars;

    @Column(name ="user_id")
    private int userId;

    private String comments;

    // Empty Constructor for the Spring Data JPA
    public Rating(){}

    // Setters and getters
    public int getRatingId(){return ratingId;}
    public String getMediaId(){return mediaId;}
    public int getStars(){return stars;}
    public int getUserId(){return userId;}
    public String getComments(){return comments;}

    public void setRatingId(int ratingId){this.ratingId = ratingId;}
    public void setMediaId(String mediaId){this.mediaId = mediaId;}
    public void setStars(int stars){this.stars = stars;}
    public void setUserId(int userId){this.userId = userId;}
    public void setComments(String comments){this.comments = comments;}

}
