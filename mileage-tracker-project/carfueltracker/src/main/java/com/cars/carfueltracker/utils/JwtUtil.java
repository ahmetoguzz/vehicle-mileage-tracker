package com.cars.carfueltracker.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey = "5f4218fa090626509d5f8b068e03996d49fd90b52a9afeb4678acf69421974de";

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // Use raw bytes for signing
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes()) // Use raw bytes for parsing
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
