# 개요

회원 가입 및 로그인, 세액 및 퇴직 연금세액 공제 금액 계산 기능 구현 프로젝트

Use: JAVA Spring, JAR, JPA, Junit, Swagger, H2 DB

Goal: 회원 가입 및 로그인 기능 구현, 회원 정보 조회 및 관리 기능 구현, 스크랩 기능 구현, 세액 및 퇴직 연금세액 공제금액 계산 기능 구현

## 기능
- [X] 회원 가입
- [X] 로그인
- [X] 내 정보 보기
- [X] 스크랩
- [X] 결정 세액 및 퇴직 연금세액 공제금액 계산

## 내용
### API 목록 및 상세 내용
 1) 회원 가입 API
   - 엔드포인트: /szs/signup

   - method: POST

   - 필수 파라미터: userId, password, name, regNo

   - 요청 파라미터 예시: {"userId" : "hong12", "password" : "123456", "name" : "홍길동", "regNo" : "860824-1655068"}

 2) 로그인 API
   - 엔드포인트: /szs/login

   - method: POST

   - 필수 파라미터: userId, password

   - 요청 파라미터 예시: {"userId" : "hong12", "password" : "123456"}

 3) 내 정보 보기 API
   - 엔드포인트: /szs/me

   - method: GET

   - 필수 파라미터: JWT Token

   - 요청 파라미터 예시: header Authorization: Bearer JwtToken

 4) 스크랩 API
   - 엔드포인트: /szs/scrap

   - method: POST

   - 필수 파라미터: JWT Token

   - 요청 파라미터 예시: header Authorization: Bearer JwtToken

 5) 결정 세액 및 퇴직 연금세액 공제금액 계산 API
   - 엔드포인트: /szs/refund

   - method: GET

   - 필수 파라미터: JWT Token

   - 요청 파라미터 예시: header Authorization: Bearer JwtToken

