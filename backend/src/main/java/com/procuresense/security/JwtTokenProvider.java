package com.procuresense.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey signingKey;

    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${app.security.jwt.secret}") String jwtSecret,
            @Value("${app.security.jwt.expiration:86400000}") long expirationMs) {

        if (jwtSecret == null || jwtSecret.length() < 64) {
            throw new IllegalArgumentException(
                    "JWT secret must be at least 64 characters long for HS512"
            );
        }

        this.signingKey = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );

        this.expirationMs = expirationMs;
    }

    public String generateToken(
            Long userId,
            String email,
            String role) {

        Date now = new Date();

        Date expiry
                = new Date(
                        now.getTime() + expirationMs
                );

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    public String getEmailFromToken(
            String token) {

        return getClaims(token)
                .getSubject();
    }

    public Long getUserIdFromToken(
            String token) {

        Object value
                = getClaims(token)
                        .get("userId");

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        return Long.valueOf(value.toString());
    }

    public String getRoleFromToken(
            String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    public boolean validateToken(
            String token) {

        try {

            getClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    private Claims getClaims(
            String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
