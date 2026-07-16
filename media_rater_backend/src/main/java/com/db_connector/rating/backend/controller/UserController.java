package com.db_connector.rating.backend.controller;

import com.db_connector.rating.backend.service.UserService;
import com.db_connector.rating.backend.model.AppUser;
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
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * =============================== FIND STATEMENTS ===============================
     */

    @GetMapping("/userById")
    public ResponseEntity<?> getUserById(@RequestParam int userId) {
        try {
            AppUser user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
    }

    @GetMapping("/checkUsername")
    public ResponseEntity<Boolean> existsByUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    

    @GetMapping("/findUsers")
    public ResponseEntity<?> findByUsername(@RequestParam String username) {
        try {
            AppUser user = userService.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User does not exist with such username."));
            
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<AppUser>> findAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    /**
     * ======================== TRANSACTIONAL STATEMENTS ========================
     */

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteByUsername(@RequestParam String username) {
        try {
            userService.deleteByUsername(username);
            return ResponseEntity.ok("User '" + username + "' deleted successfully.");
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody AppUser newUser){
        try {
            AppUser currUser = userService.createUser(newUser);
            return ResponseEntity.ok(currUser);
        } catch (IllegalArgumentException ie){
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam int userID,
        @RequestParam String currentPass, @RequestParam String newPass)
    {
        try {
            AppUser currUser = userService.changePassword(userID, currentPass, newPass);
            return ResponseEntity.ok(currUser);
        } catch (IllegalArgumentException ie){
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            AppUser currUser = userService.login(
                loginRequest.getUsername(), 
                loginRequest.getPassword()
            );
            return ResponseEntity.ok(currUser);
        } catch (IllegalArgumentException ie){
            return ResponseEntity.badRequest().body(ie.getMessage());
        }
    }
}
