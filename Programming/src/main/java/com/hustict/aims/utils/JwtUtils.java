package com.hustict.aims.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

@Component
public class JwtUtils {
    private static final long TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;
    
    private final SecretKey secretKey;

    public JwtUtils() {
        // Generate a secure key that meets HS256 requirements (256 bits)
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // Generate JWT token with user information
    public String generateToken(String userId, Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + TOKEN_EXPIRATION);
        
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract claims from JWT token
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}