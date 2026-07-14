package com.db_connector.rating.backend.service;

import org.springframework.stereotype.Service;
import com.db_connector.rating.backend.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import com.db_connector.rating.backend.model.AppUser;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<AppUser> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public AppUser getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));
    }

    public boolean userExists(int userId) {
        return userRepository.existsById(userId);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public List<AppUser> findAllUsers(){
        return userRepository.findAll();
    }

    @Transactional
    public void deleteByUsername(String username) throws IllegalArgumentException {
        if (userRepository.existsByUsername(username)){
            userRepository.deleteByUsername(username);
        } else {
            throw new IllegalArgumentException("Username does not exist.");
        }
    }
    
    public AppUser createUser(AppUser newUser) throws IllegalArgumentException {
        if (userRepository.existsByUsername(newUser.getUsername())){
            throw new IllegalArgumentException("Username is already taken.");
        }

        return userRepository.save(newUser);
    }

    public AppUser changePassword(int userID, String currentPass, String newPass){
    
        AppUser currUser = userRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist with such id."));

        if (!currUser.getPassword().equals(currentPass)) {
            throw new IllegalArgumentException("The current password you entered is incorrect.");
        }

        currUser.setPassword(newPass);

        return userRepository.save(currUser);
    }

    public AppUser login(String username, String password){
        AppUser currUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist with such username."));

        if (!currUser.getPassword().equals(password)) {
            throw new IllegalArgumentException("Incorrect password.");
        }
    
        return currUser;
    }   
}
