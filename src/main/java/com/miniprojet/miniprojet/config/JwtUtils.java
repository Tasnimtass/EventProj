package com.miniprojet.miniprojet.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import com.miniprojet.miniprojet.entity.Utilisateur;

@Component
public class JwtUtils {
    private static final long EXPIRATION_TIME = 864_000_00; //en seconde cette nmbr il est 24 h
    private static final String JWT_SECRET = "6TUcmFGhX78xpYxZL9P3VXbB7qJdM7dLArJvwh9GzUjcCZ6M4Q3mRaFBVUYxTQ9H"; // au moins 64 caractères
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(Utilisateur user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("roles", user.getRoles().stream()
                        .map(role -> role.getName().name()) // Doit retourner "ORGANISATEUR"
                        .collect(Collectors.toList()))
                .claim("nom", user.getNom())
                .claim("id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String extractEmail(String token) {
        try {
            System.out.println("JWT reçu: " + token);
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            System.out.println("Erreur lors du parsing du token : " + e.getMessage());
            e.printStackTrace(); // ou logguer avec un logger
            return null;
        }
    }
}