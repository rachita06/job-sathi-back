package com.example.jobsathi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by Rabindra Adhikari on 2/26/26
 */
@Configuration
public class RestClientConfig {
    @Bean
    public RestClient checkGrammar() {
        return RestClient.builder()
                .baseUrl("https://api.languagetool.org/v2/check")
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }
}
