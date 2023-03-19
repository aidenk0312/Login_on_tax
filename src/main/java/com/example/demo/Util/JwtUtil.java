package com.example.demo.Util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;


public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String createToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .signWith(SECRET_KEY)
                .compact();
    }
}