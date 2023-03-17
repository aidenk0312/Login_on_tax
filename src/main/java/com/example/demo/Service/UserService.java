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

    public Users signUp(Users user) {
        if (isValidUser(user)) {
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    private boolean isValidUser(Users user) {
        // 예시로 제시된 사용자
        List<String> validNames = Arrays.asList("홍길동", "김둘리", "마징가", "베지터", "손오공");
        List<String> validRegNos = Arrays.asList("860824-1655068", "921108-1582816", "880601-2455116", "910411-1656116", "820326-2715702");

        // 사용자 이름 및 주민등록번호 유효성 검사 부분
        int index = validNames.indexOf(user.getName());
        if (index != -1 && validRegNos.get(index).equals(user.getRegNo())) {
            // 중복 아이디 검사 부분
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
}
