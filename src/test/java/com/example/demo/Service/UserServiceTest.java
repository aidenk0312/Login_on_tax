package com.example.demo.Service;

import com.example.demo.Controller.UserController;
import com.example.demo.User.Users;
import com.example.demo.Util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private Users user;

    @BeforeEach
    void setUp() {
        user = new Users("hong12", "123456", "홍길동", "860824-1655068");
    }

    @Test
    void signUp_Success() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);

        when(userService.signUp(any(Users.class))).thenReturn(response);

        mockMvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void signUp_Failed() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "가입 대상이 아닙니다.");

        when(userService.signUp(any(Users.class))).thenReturn(response);

        mockMvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("가입 대상이 아닙니다."));
    }

    @Test
    void login_Success() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("token", "token_hong12_123456");
        response.put("message", "로그인 성공");

        when(userService.login(user.getUserId(), user.getPassword())).thenReturn(response);

        mockMvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token_hong12_123456"))
                .andExpect(jsonPath("$.message").value("로그인 성공"));
    }

    @Test
    void login_PW_Failed() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", "비밀번호가 틀렸습니다.");

        when(userService.login(user.getUserId(), "wrongPassword")).thenReturn(response);

        Users wrongPasswordUser = new Users(user.getUserId(), "wrongPassword", user.getName(), user.getRegNo());

        mockMvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongPasswordUser)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_Id_Failed() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put("message", "아이디가 틀렸습니다.");

        when(userService.login("wrongUserId", user.getPassword())).thenReturn(response);

        Users wrongUserIdUser = new Users("wrongUserId", user.getPassword(), user.getName(), user.getRegNo());

        mockMvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(wrongUserIdUser)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("아이디가 틀렸습니다."));
    }
    @Test
    void getMyInfo_Success() throws Exception {
        String token = "token_hong12_123456";
        when(jwtTokenUtil.getUserIdFromToken(token)).thenReturn(user.getUserId());
        when(userService.getUserById(user.getUserId())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/szs/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.regNo").value(user.getRegNo()));
    }

    @Test
    void getMyInfo_Failed() throws Exception {
        String token = "invalid_token";
        when(jwtTokenUtil.getUserIdFromToken(token)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/szs/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}