package com.example.demo.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.security.Key;
import java.util.Date;
import javax.annotation.PostConstruct;

@Component
public class JwtTokenUtil {

    private static Key secret;

    private static long expiration;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        expiration = Long.parseLong(env.getProperty("jwt.expiration"));
        logger.info("Secret key: " + secret);
    }

    public static String generateToken(String userId) {
        logger.info("Secret key: " + secret);
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secret)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().before(new Date());
    }
}