package com.example.demo.Controller;

import com.example.demo.Domain.Users;
import com.example.demo.Service.Service;
import com.example.demo.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private static final Map<String, String> allowedUsers = new HashMap<>();

    private final Service service = new Service();
    private final JwtUtil jwtUtil = new JwtUtil();

    static {
        allowedUsers.put("홍길동", "860824-1655068");
        allowedUsers.put("김둘리", "921108-158216");
        allowedUsers.put("마징가", "880601-2455116");
        allowedUsers.put("배지터", "910411-1656116");
        allowedUsers.put("손오공", "820326-2715702");
    }

    @PostMapping("/szs/signup")
    public ResponseEntity<String> signUp(@RequestBody Users user) {
        String regNo = allowedUsers.get(user.getName());

        if (regNo != null && regNo.equals(user.getRegNo())) {
            return ResponseEntity.ok("회원 가입 성공");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("회원 가입 실패: 유효하지 않은 사용자");
        }
    }

    @PostMapping("/szs/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String password = request.get("password");

        if (service.authenticate(userId, password)) {
            String token = jwtUtil.createToken(userId);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 잘못된 사용자 ID 또는 비밀번호");
        }
    }
}
