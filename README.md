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
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
메뉴 생성 | POST | /stores/{storeId}/menus | {<br> ”menu” : “SignaturePizza”,<br> ”price” : 28000,<br> ”content” : “good” <br>} | {<br> "id": 1 ”menu” : “SignaturePizza”,<br> ”price” : 28000,<br> ”content” : “good” <br>} | 201 CREATED
메뉴 수정 | PUT | /stores/{storeId}/menus/{menuId} | {<br> ”menu” : “SignaturePizza”, <br>”price” : 30000,<br> ”content” : “good and tasty” <br>} | {<br> "id": 1,<br> ”menu” : “SignaturePizza”,<br> ”price” : 30000, <br>”content” : “good and tasty” <br>} | 200 OK
메뉴 삭제 | DELETE | /stores/{storeId}/menus/{menuId} |   |   | 204 NO_CONTENT

### 카트
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
카트 메뉴 추가 | POST | /carts/items | {   "menuId": 123,   "quantity": 2 } |   | 200 OK
카트 조회 | GET | /carts |   | { ”storeId” : store_id, ”storeName” : “가게 이름”, ”items” : [   {     "cartItemId": 1,     "menuId": 123,     "menuName": "떡볶이",     "quantity": 2,     "priceSnapshot": 6000    }, /// ], "totalPrice": 12000 } | 200 OK
카트 메뉴 수량 변경 | PUT | /carts/items/{cartItemId} | {   "quantity": 2 } |   | 200 OK
카트 단일 메뉴 삭제 | DELETE | /carts/items/{cartItemId} |   |   | 200 OK
카트 전체 삭제 | DELETE | /carts |   |   | 200 OK

### 주문
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
주문(결제) | POST | /orders | { ”address”:주소 } | { "orderId": 1, "userId": 1234, "storeName": "Pizza Palace", "status": "PENDING", "items":[ { "itemId":1, "menuName":"메뉴1", "quantity":2, "price":3500, "totalPrice":quantity*price } totalPrice : 7000, "address": "대전광역시", "orderedAt":"2025-04-25", "deliveredAt": null } | 201 CREATED
주문 조회 | GET | /orders/{orderId} |   | 위와 같음 | 200 OK
주문 전체조회 | GET | /orders | {   "quantity": 2 } | [ { "orderId": 1, "storeId": 1, "storeName": "식당이름1", "menuName": "[메뉴1,메뉴2]", "totalPrice": 10000, "status": "PENDING", "orderedAt": "2025-04-25T18:53:02.636048" }, { "orderId": 2, "storeId": 2, "storeName": "식당이름2", "menuName": "[메뉴3,메뉴4]", "totalPrice": 10000, "status": "PENDING", "orderedAt": "2025-04-25T18:53:02.636048" } ] | 200 OK
주문 수정 | PATCH | /orders/{orderId} |   | { "orderId": 1, "userId": 1234, "storeName": "Pizza Palace", "status": COOKING, "items":[ ….} | 200 OK
주문 취소 | DELETE | /orders/{orderId} |   | orderStatus : PENDING→ CALCEL | 200 OK

### 리뷰
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
리뷰작성 | POST | /orders/{orderId}/reviews | { ”rating”:별점 ”content”:리뷰 } | { ”reviewId”: 1, ”storeName” : “가게명”, ”rating” : 5, ”content”: “맛있어요.” ”createdAt”:”2025-04-25” } | 201 CREATED
내리뷰보기 | GET | /users/me/reviews |   | [ { ”reviewId”: 1, ”storeName” : “가게명”, ”rating” : 5, ”content”: “맛있어요.” ”createdAt”:”2025-04-25” }, { ”reviewId”: 2, ”storeName” : “가게명2”, ”rating” : 1, ”content”: “맛없어요.” ”createdAt”:”2025-04-25” } ] | 200 OK
가게리뷰조회 | GET | /stores/{storeId}/reviews |   | 위와 같음 | 200 OK
가게리뷰평점별로조회 | GET | /stores/{storeId}/reviews", params = {"minRating", "maxRating"} |   | 위와 같음 | 200 OK
리뷰삭제 | DELETE | /reviews/{reviewId} |   |   | 200 OK

### 대시보드
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
대시보드 메인 | GET | /dashboard | {   “stores” : {      ”store1”: [       “(category)”, “(ads)”, “(sales), …”],     “store2”:[ … ] }} |   |  
관리자 페이지 | GET | /admin |   |   | 200 OK
가게 상세 | GET | /dashboard/{storeId} |   |   |  
광고 조회 | GET | /dashboard/{storeId}/ads |   |   |  
광고 생성 | POST | /dashboard/{storeId}/ads |   |   |  
광고 편집 | PATCH | /dashboard/{storeId}/ads/{adId} |   |   |  
광고 초기화 | DELETE | /dashboard/{storeId}/ads/ |   |   |  
광고 삭제 | DELETE | /dashboard/{storeId}/ads/{adId} |   |   |  
