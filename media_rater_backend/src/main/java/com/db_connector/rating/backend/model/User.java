package com.db_connector.media_rater.backend.model;

public class User {
    private String username;
    private String password;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public void changeUsername(String newUsername){
        this.username = newUsername;
    }

    public String password(){
        return password;
    }
}
