package com.example.demo.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User signUp(User user) {
        if (isValidUser(user)) {
            // 암호화는 User 클래스의 setter 메소드에서 처리했습니다.
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    private boolean isValidUser(User user) {
        // 예시로 제시된 사용자 정보
        List<String> validNames = Arrays.asList("홍길동", "김둘리", "마징가", "베지터", "손오공");
        List<String> validRegNos = Arrays.asList("860824-1655068", "921108-1582816", "880601-2455116", "910411-1656116", "820326-2715702");

        // 사용자 이름 및 주민등록번호 유효성 검사
        int index = validNames.indexOf(user.getName());
        if (index != -1 && validRegNos.get(index).equals(user.getRegNo())) {
            // 중복 아이디 검사
            return userRepository.findByUserId(user.getUserId()).isEmpty();
        }
        return false;
    }
}
