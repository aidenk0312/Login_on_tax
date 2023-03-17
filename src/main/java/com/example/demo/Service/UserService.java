package com.example.demo.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> signUp(Users user) {
        Map<String, Object> response = new HashMap<>();
        if (isValidUser(user)) {
            Users savedUser = userRepository.save(user);
            response.put("user", savedUser);
        } else {
            response.put("message", "가입 대상이 아닙니다.");
        }
        return response;
    }

    private boolean isValidUser(Users user) {
        // 예시로 제시된 사용자
        List<String> validNames = Arrays.asList("홍길동", "김둘리", "마징가", "베지터", "손오공");
        List<String> validRegNos = Arrays.asList("860824-1655068", "921108-1582816", "880601-2455116", "910411-1656116", "820326-2715702");

        int index = validNames.indexOf(user.getName());
        if (index != -1 && validRegNos.get(index).equals(user.getRegNo())) {
            return userRepository.findByUserId(user.getUserId()).isEmpty();
        }
        return false;
    }

    public Map<String, String> login(String userId, String password) {
        Map<String, String> response = new HashMap<>();
        Optional<Users> optionalUser = userRepository.findByUserId(userId);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            if (user.getPassword().equals(password)) {
                response.put("token", "token_" + userId + "_" + System.currentTimeMillis());
                response.put("message", "로그인 성공");
            } else {
                response.put("message", "비밀번호가 틀렸습니다.");
            }
        } else {
            response.put("message", "아이디가 틀렸습니다.");
        }
        return response;
    }

    public Users getUserById(String userId) {
        Optional<Users> optionalUser = userRepository.findByUserId(userId);
        return optionalUser.orElse(null);
    }
}
