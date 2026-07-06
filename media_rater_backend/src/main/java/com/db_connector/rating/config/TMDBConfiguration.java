package com.db_connector.rating.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TMDBConfiguration {
    @Bean
    public RestClient tmdbRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.themoviedb.org/3")
                .defaultHeader("accept", "application/json")
                .build();
    }

}
