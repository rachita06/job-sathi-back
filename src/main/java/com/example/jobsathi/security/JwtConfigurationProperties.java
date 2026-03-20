package com.example.jobsathi.security;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Created by Rabindra Adhikari on 3/20/26
 */

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Validated
@Getter
@Setter
public class JwtConfigurationProperties {
    @NotBlank(message = "JWT secret must not be blank")
    private String secret;
    private long expiration;
}
