package com.example.spring_jwt_auth.Security;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt-secret}")
    private String jwtSecrert;

    @Value("${app-jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecrert));
    }

    // generate JWT Token
    public String generateTOken(Authentication authentication){
        String username = authentication.getName();

        Date currentDate = new Date();

        Instant now = Instant.now();
        Date expireDate =  new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
    }
}
