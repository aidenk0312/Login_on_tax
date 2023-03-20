package com.example.demo.Util;

public class TestTokenUtil {
    public static String generateTestToken(String userId) {
        JwtUtil jwtUtil = new JwtUtil();
        return jwtUtil.createToken(userId);
    }
}