package com.example.demo.Controller;

import com.example.demo.Domain.ScrapData;
import com.example.demo.Domain.Users;
import com.example.demo.Service.ScrapDataService;
import com.example.demo.Service.UsersService;
import com.example.demo.Util.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private static final Map<String, String> allowedUsers = new HashMap<>();

    private final UsersService service = new UsersService();
    private final JwtUtil jwtUtil = new JwtUtil();
    private final ScrapDataService scrapDataService;

    static {
        allowedUsers.put("홍길동", "860824-1655068");
        allowedUsers.put("김둘리", "921108-1582816");
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

    @GetMapping("/szs/me")
    public ResponseEntity<?> getMyInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder().setSigningKey(jwtUtil.getSecretKey()).build().parseClaimsJws(jwtToken).getBody();
                String userId = claims.getSubject();

                Users user = service.getUserById(userId);
                if (user != null) {
                    return ResponseEntity.ok(user);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
                }
            } catch (JwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 인증 토큰입니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 토큰이 필요합니다.");
        }
    }

    @PostMapping("/szs/scrap")
    public ResponseEntity<?> scrap(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, String> request) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder().setSigningKey(jwtUtil.getSecretKey()).build().parseClaimsJws(jwtToken).getBody();
                String userId = claims.getSubject();

                Users user = service.getUserById(userId);
                if (user != null) {
                    String name = request.get("name");
                    String regNo = request.get("regNo");

                    if (name.equals(user.getName()) && regNo.equals(user.getRegNo())) {
                        RestTemplate restTemplate = new RestTemplate();
                        String scrapUrl = "https://codetest.3o3.co.kr/v2/scrap";
                        Map<String, String> scrapRequest = new HashMap<>();
                        scrapRequest.put("name", name);
                        scrapRequest.put("regNo", regNo);

                        try {
                            ResponseEntity<String> scrapResponse = restTemplate.postForEntity(scrapUrl, scrapRequest, String.class);

                            ScrapData scrapData = new ScrapData();
                            scrapData.setUserId(userId);
                            scrapData.setName(name);
                            scrapData.setRegNo(regNo);
                            scrapData.setJsonList(scrapResponse.getBody());

                            scrapDataService.saveScrapData(scrapData);

                            String formattedResponse = scrapData.formatJson(scrapResponse.getBody());
                            return ResponseEntity.ok(formattedResponse);
                        } catch (RestClientException e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("스크랩 요청 중 오류가 발생했습니다: " + e.getMessage());
                        } catch (Exception e) {
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다: " + e.getMessage());
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증된 사용자의 정보만 스크랩할 수 있습니다.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
                }
            } catch (JwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 인증 토큰입니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 토큰이 필요합니다.");
        }
    }

    @GetMapping("/szs/refund")
    public ResponseEntity<?> calculateRefund(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwtToken = authHeader.substring(7);

                Claims claims = Jwts.parserBuilder().setSigningKey(jwtUtil.getSecretKey()).build().parseClaimsJws(jwtToken).getBody();
                String userId = claims.getSubject();

                Users user = service.getUserById(userId);
                if (user != null) {
                    ScrapData scrapData = scrapDataService.getScrapDataByUserId(userId);
                    if (scrapData != null) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode scrapDataJson = objectMapper.readTree(scrapData.getJsonList());
                        JsonNode jsonList = scrapDataJson.get("data").get("jsonList");

                        int 산출세액 = scrapData.getJsonValueAsIntSafely("산출세액");
                        int 퇴직연금납입금액 = scrapData.getJsonValueAsIntSafely("퇴직연금납입금액");
                        int 보험료납입금액 = scrapData.getJsonValueAsIntSafely("보험료납입금액");
                        int 의료비납입금액 = scrapData.getJsonValueAsIntSafely("의료비납입금액");
                        int 교육비납입금액 = scrapData.getJsonValueAsIntSafely("교육비납입금액");
                        int 기부금납입금액 = scrapData.getJsonValueAsIntSafely("기부금납입금액");
                        int 총급여 = scrapData.getJsonValueAsIntSafely("총급여");

                        int 근로소득세액공제금액 = (int) (산출세액 * 0.55);
                        int 퇴직연금세액공제금액 = (int) (퇴직연금납입금액 * 0.15);
                        int 보험료공제금액 = (int) (보험료납입금액 * 0.12);
                        int 의료비공제금액 = (int) Math.max((의료비납입금액 - 총급여 * 0.03) * 0.15, 0);
                        int 교육비공제금액 = (int) (교육비납입금액 * 0.15);
                        int 기부금공제금액 = (int) (기부금납입금액 * 0.15);
                        int 특별세액공제금액 = 보험료공제금액 + 의료비공제금액 + 교육비공제금액 + 기부금공제금액;
                        int 표준세액공제금액 = 특별세액공제금액 < 130000 ? 130000 : 0;
                        if (표준세액공제금액 == 130000) {
                            특별세액공제금액 = 0;
                        }

                        int 결정세액 = 산출세액 - 근로소득세액공제금액 - 특별세액공제금액 - 표준세액공제금액 - 퇴직연금세액공제금액;
                        if (결정세액 < 0) {
                            결정세액 = 0;
                        }

                        Map<String, String> responseData = new HashMap<>();
                        responseData.put("이름", user.getName());
                        responseData.put("결정세액", String.format("%,d", 결정세액));
                        responseData.put("퇴직연금세액공제", String.format("%,d", 퇴직연금세액공제금액));

                        return ResponseEntity.ok(responseData);
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }
}