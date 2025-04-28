# 아웃소싱 프로젝트 🛵💨
아웃소싱 컨셉의 배달 어플리케이션 구현 프로젝트 <br>
회원가입/로그인 및 가게, 메뉴, 주문, 리뷰 CRUD 기능을 제공합니다. <br>

## 🛠️ 개발 환경
**IDE** : IntelliJ <br>
**프레임워크** : Spring Boot 3.4.4 <br>
**데이터베이스** : MySQL / JPA <br>
**빌드 도구** : Gradle-Groovy <br>
**언어** : Java 17 <br>
**의존성** : Lombok / Spring Web / JDBC API / Spring Data JPA / MySQL Driver / Validation / Java Mail Sender <br>
**테스트 도구** : Postman <br>

## 🕺 역할 분담
기능 | 담당
-- | --
회원가입 / 로그인 | 김정연
메뉴 / 가게 CRUD | 이진아
주문 / 리뷰 CRUD | 류형철
부가서비스 / 광고 / 대시보드 | 이형진
소셜 로그인 / 장바구니 / 알림 | 이수빈
테스트코드 및 예외 | 공통사항

## ⛓️ ERD
<img width="1066" alt="Image" src="https://github.com/user-attachments/assets/9e0de846-6c0e-47a0-b9e0-08ce4ddd5d4a" />

## 🗒️　API
### 회원관리
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
회원가입 | POST | /auth/signup |   |   | 201 CREATED
회원탈퇴 | DELETE | /auth/account |   |   | 200 OK
로그인 | POST | /auth/login |   | {   “accessToken” : “(String \| Token)” } | 200 OK

### 가게
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
가게 생성 | POST | /stores | { <br>"name" : "zzangpizza",<br> "openingTime" : "10:00", <br>"closingTime": "22:00", <br>"minOrderValue" : 30000 <br>} | {<br> "id": 1,<br> "name":"zzangpizza",<br> "openingTime" : "10:00",<br> "closingTime": "22:00", <br>"minOrderValue" : 30000 <br>} | 201 CREATED
가게 검색 | GET | /stores |   | [<br> {<br> "id": 1,<br> "name": "zzangpizza",<br> "openingTime": "10:00:00",<br> "closingTime": "22:30:00", <br>"minOrderValue": 30000 }, <br>{ <br>"id": 2,<br> "name": "goodpizza", <br>"openingTime": "10:00:00",<br> "closingTime": "22:00:00",<br> "minOrderValue": 28000 <br>}, <br>] | 200 OK
가게 상세 | GET | /stores/{storeId} |   | { <br>"id": 1,<br> "name": "zzangpizza",<br> "openingTime": "10:00:00", <br>"closingTime": "22:30:00", <br>"minOrderValue": 30000,<br> "menuList": <br>[ <br>{ <br>"id": 1, <br>"name": "SignaturePizza",<br> "price": 28000,<br> "content": "good" <br>}, <br>{<br> "id": 2, <br>"name": "SpicyPizza", <br>"price": 26000, <br>"content": "spicy"<br> } <br>] <br>} | 200 OK
가게 수정 | PUT | /stores/{storeId} | {<br> "name" : "zzangpizza2", <br>"openingTime" : "10:00",<br> "closingTime": "24:00",<br> "minOrderValue" : 30000 <br>}<br> | {<br> "name" : "zzangpizza2",<br> "openingTime" : "10:00", <br>"closingTime": "24:00",<br> "minOrderValue" : 30000 <br>} | 200 OK
가게 삭제 | DELETE | /stores/{storeId} |   |   | 204 NO_CONTENT
### 메뉴
### 카트
### 주문
### 리뷰
### 대시보드
