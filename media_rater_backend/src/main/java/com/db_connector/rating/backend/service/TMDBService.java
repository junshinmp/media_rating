package com.db_connector.rating.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TMDBService {
    @Value("${tmdb.api.token}")
    private String api_token;

    private final RestClient restClient;

    public TMDBService(RestClient restClient){
        this.restClient = restClient;
    }

    public String searchMedia(String title){
        return restClient.get()
                .uri("/search/multi?query={query}&include_adult=true&language=en-US&page=1", title)
                .header("Authorization", "Bearer " + api_token)
                .retrieve()
                .body(String.class);
    }

    public String mediaFromId(String id) {
        return restClient.get()
                    .uri("/movie/{id}?language=en-US", id)
                    .header("Authorization", "Bearer " + api_token)
                    .retrieve()
                    .body(String.class);
    }
}
