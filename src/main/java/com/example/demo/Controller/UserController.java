package com.example.demo.Controller;

import com.example.demo.Service.UserService;
import com.example.demo.User.Users;
import com.example.demo.Util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/szs/signup")
    public ResponseEntity<Object> signUp(@RequestBody Users user) {
        Map<String, Object> response = userService.signUp(user);

        if (response.containsKey("user")) {
            return new ResponseEntity<>(response.get("user"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response.get("message"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/szs/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Users user) {
        Map<String, String> response = userService.login(user.getUserId(), user.getPassword());

        if (response.containsKey("token")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/szs/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String userId = jwtTokenUtil.getUserIdFromToken(token);

            Users user = userService.getUserById(userId);

            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
