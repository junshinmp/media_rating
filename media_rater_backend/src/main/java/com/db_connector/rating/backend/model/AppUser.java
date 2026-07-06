package com.db_connector.rating.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    // Default constructor for Spring Data JBA
    public AppUser(){}

    // setters and getters
    public int getUserId(){ return userId;}
    public String getUserName(){ return username;}
    public String getFirstName(){ return firstName;}
    public String getLastName(){ return lastName;}
    public String getPassword(){ return password;}
    
    public void setUserId(int userId){ this.userId = userId;}
    public void getUserName(String username){ this.username = username;}
    public void getfirstName(String firstName){ this.firstName = firstName;}
    public void getlastName(String lastName){ this.lastName = lastName;}
    public void getPassword(String password){ this.password = password;}
}
