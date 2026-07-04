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
    @Column(name = "media_id")
    private int mediaId;

    @Column(name = "media_title")
    private String mediaTitle;

    // User related data
    private int stars;

    @Column(name ="user_id")
    private int userId;

    private String comments;


    // Empty Constructor for the Spring Data JPA
    public Rating(){}

    // Setters and getters
    public int getRatingId(){return ratingId;}
    public int getMediaId(){return mediaId;}
    public String getMediaTitle(){return mediaTitle;}
    public int getStars(){return stars;}
    public int getUserId(){return userId;}
    public String getComments(){return comments;}

    public void setRatingId(int ratingId){this.ratingId = ratingId;}
    public void setMediaId(int mediaId){this.mediaId = mediaId;} 
    public void setMediaTitle(String mediaTitle){this.mediaTitle = mediaTitle;}
    public void setStars(int stars){this.stars = stars;}
    public void setUserId(int userId){this.userId = userId;}
    public void setComments(String comments){this.comments = comments;}

}
