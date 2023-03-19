package com.example.demo.Controller;

import com.example.demo.Domain.Users;
import com.example.demo.Util.JwtUtil;
import com.example.demo.Service.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    private String jwtToken;

    @MockBean
    private UsersService usersServiceService;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        Users testUser = new Users("hong12", "123456", "홍길동", "860824-1655068");
        when(usersServiceService.getUserById("hong12")).thenReturn(testUser);
    }

    @Test
    @DisplayName("회원가입 성공")
    public void signUpSuccess() throws Exception {
        Users user = Users.builder()
                .name("홍길동")
                .regNo("860824-1655068")
                .build();

        mockMvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("회원 가입 성공"));
    }

    @Test
    @DisplayName("회원가입 실패")
    public void signUpFailure() throws Exception {
        Users user = Users.builder()
                .name("이순신")
                .regNo("800101-1234567")
                .build();

        mockMvc.perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("회원 가입 실패: 유효하지 않은 사용자"));
    }

    @Test
    @DisplayName("로그인 성공")
    public void loginSuccess() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "hong12");
        params.put("password", "123456");

        mockMvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("로그인 실패")
    public void loginFailure() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("userId", "hong13");
        params.put("password", "123456");

        mockMvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("로그인 실패: 잘못된 사용자 ID 또는 비밀번호"));
    }
}