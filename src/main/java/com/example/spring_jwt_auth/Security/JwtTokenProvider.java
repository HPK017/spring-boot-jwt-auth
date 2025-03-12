package com.example.spring_jwt_auth.Security;

import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;   
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt-secret}")
    private String jwtSecrert;

    @Value("${app-jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecrert));
    }

    // generate JWT Token
    public String generateTOken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();

        return token;
    }

    // get Username from JWT Token
    public String getUsername(String token) {
        Claims claim = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claim.getSubject();
        return username;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid Jwt token {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Jwt tken is expired {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Jwt Token is unsupported {}", e.getMessage() );
        } catch (IllegalArgumentException e) {
            logger.error("Jwt Claims string is empty {}", e.getMessage());
        }
        return false;
    }

}
