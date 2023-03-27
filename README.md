### 회원 가입 및 로그인 API 프로젝트

## 1. Swagger 주소: http://localhost:8080/swagger-ui/index.html#/

## 2. 구현 방법: 이 프로젝트는 다음과 같은 API를 구현합니다:
 - 회원 가입 API
 - 로그인 API
 - 내 정보 보기 API
 - 스크랩 API
 - 결정 세액 및 퇴직 연금세액 공제금액 계산 API
 - 각 API는 RESTful 방식으로 구현되어 있으며, 인증을 위해 JWT를 사용합니다.

## 3. 검증 결과
  - 프로젝트를 통해 각 API의 기능이 정상적으로 동작하고, 요구사항이 정확하게 구현되었음을 확인하였습니다.

## 4. API 목록 및 상세 내용
 - [x] 1) 회원 가입 API
  - 엔드포인트: /szs/signup
  - method: POST
  - 필수 파라미터: userId, password, name, regNo
  - 요청 파라미터 예시: {"userId" : "hong12", "password" : "123456", "name" : "홍길동", "regNo" : "860824-1655068"}

 - [x] 2) 로그인 API
  - 엔드포인트: /szs/login
  - method: POST
  - 필수 파라미터: userId, password
  - 요청 파라미터 예시: {"userId" : "hong12", "password" : "123456"}
 
 - [x] 3) 내 정보 보기 API
  - 엔드포인트: /szs/me
  - method: GET
  - 필수 파라미터: JWT Token
  - 요청 파라미터 예시: header Authorization: Bearer JwtToken

 - [x] 4) 스크랩 API
  - 엔드포인트: /szs/scrap
  - method: POST
  - 필수 파라미터: JWT Token
  - 요청 파라미터 예시: header Authorization: Bearer JwtToken

 - [x] 5) 결정 세액 및 퇴직 연금세액 공제금액 계산 API
  - 엔드포인트: /szs/refund
  - method: GET
  - 필수 파라미터: JWT Token
  - 요청 파라미터 예시: header Authorization: Bearer JwtToken

## 5. 구현 시 중요하게 고려한 부분
 - RestController 사용: Spring Boot에서 제공하는 @RestController 어노테이션을 사용하여 HTTP 요청을 처리하는 클래스를 구현했습니다.
 - 의존성 주입: 생성자를 통해 필요한 서비스와 유틸리티 클래스의 인스턴스를 주입받아 사용하였습니다.
 - JWT 인증: JWT(Json Web Token)를 사용하여 사용자 인증을 구현하였습니다.
 - 예외 처리: API 호출 과정에서 발생할 수 있는 예외를 적절하게 처리하여 사용자에게 명확한 오류 메시지를 전달하였습니다.
