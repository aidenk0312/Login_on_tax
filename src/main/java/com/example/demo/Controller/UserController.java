package com.example.demo.Controller;

import com.example.demo.Service.UserService;
import com.example.demo.User.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/szs/signup")
    public ResponseEntity<Users> signUp(@RequestBody Users user) {
        Users createdUser = userService.signUp(user);

        if (createdUser != null) {
            // 회원 가입 성공
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } else {
            // 회원 가입 실패 (예: 유효하지 않은 사용자 정보)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
