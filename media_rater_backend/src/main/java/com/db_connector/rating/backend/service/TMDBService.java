package com.db_connector.rating.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
        try {
            // Default to movie lookup
            return restClient.get()
                    .uri("/movie/{id}?language=en-US", id)
                    .header("Authorization", "Bearer " + api_token)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException.NotFound e) {
            // Fallback to TV show lookup if movie fails
            return restClient.get()
                    .uri("/tv/{id}?language=en-US", id)
                    .header("Authorization", "Bearer " + api_token)
                    .retrieve()
                    .body(String.class);
        }
    }

    public boolean mediaExists(String mediaId){
        if (mediaId == null || mediaId.trim().isEmpty()) {
            return false;
        }
        
        try {
            restClient.get()
                    .uri("/movie/{id}", mediaId)
                    .header("Authorization", "Bearer " + api_token)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound e){
            try {
                restClient.get()
                        .uri("/tv/{id}", mediaId)
                        .header("Authorization", "Bearer " + api_token)
                        .retrieve()
                        .toBodilessEntity();
                return true;
            } catch (HttpClientErrorException.NotFound ex) {
                return false;
            } catch (Exception ex){
                return false;
            }
        } catch (Exception ex){
            return false;
        }
    }
}
