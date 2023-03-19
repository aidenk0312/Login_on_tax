package com.example.demo.Service;

import com.example.demo.Domain.Users;

import java.util.HashMap;
import java.util.Map;

public class UsersService {
    private static final Map<String, Users> users = new HashMap<>();

    static {
        users.put("hong12", new Users("hong12", "123456", "홍길동", "860824-1655068"));
        users.put("kim12", new Users("kim12", "654321", "김둘리", "921108-158216"));
        users.put("ma12", new Users("ma12", "789123", "마징가", "880601-2455116"));
        users.put("ba12", new Users("ba12", "456123", "배지터", "910411-1656116"));
        users.put("son", new Users("son", "987321", "손오공", "820326-2715702"));
    }

    public boolean authenticate(String userId, String password) {
        Users user = users.get(userId);
        return user != null && user.getPassword().equals(password);
    }

    public Users getUserById(String userId) {
        return users.get(userId);
    }
}