/**
 * File: ApplicationLauncher.java
 * @author: Junshin Purganan
 * 
 * This file is used in order to launch the Spring Boot application
 * for the program to work, waking up maven and related elements.
 */

package com.db_connector.rating;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationLauncher {
    public static void main(String[] args) {
        // Load .env file variables into System properties
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // Prevents crashes if deployed elsewhere without a physical .env file
                .load();
        
        dotenv.entries().forEach(entry -> 
            System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(ApplicationLauncher.class, args);
    }
}