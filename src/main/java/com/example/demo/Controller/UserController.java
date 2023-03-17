package com.example.demo.Controller;

import com.example.demo.Service.UserService;
import com.example.demo.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/szs/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Users user) {
        Map<String, String> response = userService.login(user.getUserId(), user.getPassword());

        if (response.containsKey("token")) {
            // 로그인 성공
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // 로그인 실패 (예: 잘못된 사용자 ID 또는 비밀번호)
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
