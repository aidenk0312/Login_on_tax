package com.example.demo.Controller;

import com.example.demo.Domain.ScrapData;
import com.example.demo.Domain.Users;
import com.example.demo.Service.ScrapDataService;
import com.example.demo.Service.UsersService;
import static com.example.demo.Util.TestTokenUtil.generateTestToken;

import com.example.demo.Util.TestTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpMethod;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UsersService service;

    private RestTemplate restTemplate;
    private MockRestServiceServer mockRestServiceServer;

    @Mock
    private ScrapDataService scrapDataService;

    @BeforeEach
    public void setUp() {
        Users testUser = new Users("hong12", "123456", "홍길동", "860824-1655068");
        when(service.getUserById("hong12")).thenReturn(testUser);

        restTemplate = new RestTemplate();
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
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
        String testToken = generateTestToken("hong12");

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
        String testToken = generateTestToken("hong12");

        Map<String, String> params = new HashMap<>();
        params.put("userId", "hong13");
        params.put("password", "123456");

        mockMvc.perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("로그인 실패: 잘못된 사용자 ID 또는 비밀번호"));
    }

    @Test
    @DisplayName("내 정보 가져오기 성공")
    public void getMyInfoSuccess() throws Exception {
        String testUserId = "hong12";
        String testToken = generateTestToken(testUserId);

        mockMvc.perform(get("/szs/me")
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(testUserId));
    }

    @Test
    @DisplayName("스크랩 성공")
    public void scrapSuccess() throws Exception {
        String testUserId = "hong12";
        String testToken = generateTestToken(testUserId);

        Users testUser = new Users("hong12", "123456", "홍길동", "860824-1655068");
        when(service.getUserById("hong12")).thenReturn(testUser);

        String scrapUrl = "https://codetest.3o3.co.kr/v2/scrap";
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(scrapUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));

        Map<String, String> params = new HashMap<>();
        params.put("name", "홍길동");
        params.put("regNo", "860824-1655068");

        mockMvc.perform(post("/szs/scrap")
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("환급액 계산 성공")
    public void calculateRefundSuccess() throws Exception {
        String testUserId = "hong12";
        String testToken = generateTestToken(testUserId);

        Users testUser = new Users("hong12", "123456", "홍길동", "860824-1655068");
        when(service.getUserById("hong12")).thenReturn(testUser);

        String scrapUrl = "https://codetest.3o3.co.kr/v2/scrap";
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(scrapUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));

        Map<String, String> params = new HashMap<>();
        params.put("name", "홍길동");
        params.put("regNo", "860824-1655068");

        mockMvc.perform(post("/szs/scrap")
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());

        ScrapData scrapData = new ScrapData();
        scrapData.setJsonList("{ \"data\": { \"jsonList\": { \"산출세액\": 100000, \"퇴직연금납입금액\": 50000, \"보험료납입금액\": 40000, \"의료비납입금액\": 30000, \"교육비납입금액\": 20000, \"기부금납입금액\": 10000, \"총급여\": 500000 } } }");
        when(scrapDataService.getScrapDataByUserId(testUserId)).thenReturn(scrapData);

        mockMvc.perform(get("/szs/refund")
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.이름").value("홍길동"))
                .andExpect(jsonPath("$.결정세액").isNotEmpty())
                .andExpect(jsonPath("$.퇴직연금세액공제").isNotEmpty());
    }
}