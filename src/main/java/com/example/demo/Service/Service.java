package com.example.demo.Service;

import java.util.HashMap;
import java.util.Map;

public class Service {
    private static final Map<String, String> users = new HashMap<>();

    static {
        users.put("hong12", "123456");
        users.put("kim12", "654321");
        users.put("ma12", "789123");
        users.put("ba12", "456123");
        users.put("son", "987321");
    }

    public boolean authenticate(String userId, String password) {
        String storedPassword = users.get(userId);
        return storedPassword != null && storedPassword.equals(password);
    }
}