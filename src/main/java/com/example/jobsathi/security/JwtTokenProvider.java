package com.example.jobsathi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by Rabindra Adhikari on 3/19/26a
 * This class helps to
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtConfigurationProperties jwtConfigurationProperties;

    // generate token for an authentication
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfigurationProperties.getExpiration());

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtConfigurationProperties.getSecret()));

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    // extract username from valid token
    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    // validate the token signature and expiration
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // parse and verify token
    private Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtConfigurationProperties.getSecret()));
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }


}
