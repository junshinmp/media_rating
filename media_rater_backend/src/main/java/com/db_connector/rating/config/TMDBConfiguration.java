package com.db_connector.rating.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TMDBConfiguration {
    @Value("${tmdb.api.key}")
    private String api_key;

    private final String url_base = "https://api.themoviedb.org/3";

    public String getApiKey(){
        return api_key;
    }

    public String getUrlBase(){
        return url_base;
    }

}
