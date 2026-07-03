/**
 * File: ApplicationLauncher.java
 * @author: Junshin Purganan
 * 
 * This file is used in order to launch the Spring Boot application
 * for the program to work, waking up maven and related elements.
 */

package com.db_connector.media_rater.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationLauncher {
    public static void main(String[] args) {
        // Runs Spring Boot to start maven and the related components.
        SpringApplication.run(ApplicationLauncher.class, args);
    }
}
