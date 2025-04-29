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
**테스트 도구** : Postman / JUnit5 / Mockito <br>

## 🕺 역할 분담
기능 | 담당
-- | --
회원가입 / 로그인 | 김정연
메뉴 / 가게 CRUD | 이진아
주문 / 리뷰 CRUD | 류형철
광고 / 전역 예외 | 이형진
소셜 로그인 / 장바구니 / 알림 | 이수빈
테스트코드 및 예외 | 공통사항

## ⛓️ ERD
<img width="1066" alt="Image" src="https://github.com/user-attachments/assets/9e0de846-6c0e-47a0-b9e0-08ce4ddd5d4a" />

## 🗒️　API
### < 회원관리 >
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
회원가입 | POST | /auth/signup |   |   | 201 CREATED
회원탈퇴 | DELETE | /auth/account |   |   | 200 OK
로그인 | POST | /auth/login |   | {   “accessToken” : “(String \| Token)” } | 200 OK

### < 가게 >
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
가게 생성 | POST | /stores | { <br>"name" : "zzangpizza",<br> "openingTime" : "10:00", <br>"closingTime": "22:00", <br>"minOrderValue" : 30000 <br>} | {<br> "id": 1,<br> "name":"zzangpizza",<br> "openingTime" : "10:00",<br> "closingTime": "22:00", <br>"minOrderValue" : 30000 <br>} | 201 CREATED
가게 검색 | GET | /stores |   | [<br> {<br> "id": 1,<br> "name": "zzangpizza",<br> "openingTime": "10:00:00",<br> "closingTime": "22:30:00", <br>"minOrderValue": 30000 }, <br>{ <br>"id": 2,<br> "name": "goodpizza", <br>"openingTime": "10:00:00",<br> "closingTime": "22:00:00",<br> "minOrderValue": 28000 <br>}, <br>] | 200 OK
가게 상세 | GET | /stores/{storeId} |   | { <br>"id": 1,<br> "name": "zzangpizza",<br> "openingTime": "10:00:00", <br>"closingTime": "22:30:00", <br>"minOrderValue": 30000,<br> "menuList": <br>[ <br>{ <br>"id": 1, <br>"name": "SignaturePizza",<br> "price": 28000,<br> "content": "good" <br>}, <br>{<br> "id": 2, <br>"name": "SpicyPizza", <br>"price": 26000, <br>"content": "spicy"<br> } <br>] <br>} | 200 OK
가게 수정 | PUT | /stores/{storeId} | {<br> "name" : "zzangpizza2", <br>"openingTime" : "10:00",<br> "closingTime": "24:00",<br> "minOrderValue" : 30000 <br>}<br> | {<br> "name" : "zzangpizza2",<br> "openingTime" : "10:00", <br>"closingTime": "24:00",<br> "minOrderValue" : 30000 <br>} | 200 OK
가게 삭제 | DELETE | /stores/{storeId} |   |   | 204 NO_CONTENT

### < 메뉴 >
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
메뉴 생성 | POST | /stores/{storeId}/menus | {<br> ”menu” : “SignaturePizza”,<br> ”price” : 28000,<br> ”content” : “good” <br>} | {<br> "id": 1 ”menu” : “SignaturePizza”,<br> ”price” : 28000,<br> ”content” : “good” <br>} | 201 CREATED
메뉴 수정 | PUT | /stores/{storeId}/menus/{menuId} | {<br> ”menu” : “SignaturePizza”, <br>”price” : 30000,<br> ”content” : “good and tasty” <br>} | {<br> "id": 1,<br> ”menu” : “SignaturePizza”,<br> ”price” : 30000, <br>”content” : “good and tasty” <br>} | 200 OK
메뉴 삭제 | DELETE | /stores/{storeId}/menus/{menuId} |   |   | 204 NO_CONTENT

### < 카트 >
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
카트 메뉴 추가 | POST | /carts/items | {<br>   "menuId": 123,<br>   "quantity": 2 <br>} |   | 200 OK
카트 조회 | GET | /carts |   | { <br>”storeId” : store_id, <br>”storeName” : “가게 이름”,<br> ”items” :<br> [  <br> {  <br>   "cartItemId": 1,  <br>   "menuId": 123,  <br>   "menuName": "떡볶이",  <br>   "quantity": 2,   <br>  "priceSnapshot": 6000    <br>}, <br>/// <br>], <br>"totalPrice": 12000 <br>} | 200 OK
카트 메뉴 수량 변경 | PUT | /carts/items/{cartItemId} | {  <br> "quantity": 2 <br>} |   | 200 OK
카트 단일 메뉴 삭제 | DELETE | /carts/items/{cartItemId} |   |   | 200 OK
카트 전체 삭제 | DELETE | /carts |   |   | 200 OK

### < 주문 >
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
주문(결제) | POST | /orders | { <br>”address”:주소 <br>} | { <br>"orderId": 1,<br> "userId": 1234,<br> "storeName": "Pizza Palace",<br> "status": "PENDING",<br> "items":<br>[ <br>{ <br>"itemId":1, <br>"menuName":"메뉴1",<br> "quantity":2,<br> "price":3500,<br> "totalPrice":quantity*price<br> } <br>totalPrice : 7000, <br>"address": "대전광역시",<br> "orderedAt":"2025-04-25",<br> "deliveredAt": null<br> } | 201 CREATED
주문 조회 | GET | /orders/{orderId} |   | 위와 같음 | 200 OK
주문 전체조회 | GET | /orders | {  <br> "quantity": 2<br> } | [ <br>{ <br>"orderId": 1,<br> "storeId": 1,<br> "storeName": "식당이름1",<br> "menuName": "[메뉴1,메뉴2]", <br>"totalPrice": 10000,<br> "status": "PENDING",<br> "orderedAt": "2025-04-25T18:53:02.636048" <br>}, { <br>"orderId": 2,<br> "storeId": 2,<br> "storeName": "식당이름2", <br>"menuName": "[메뉴3,메뉴4]",<br> "totalPrice": 10000, <br>"status": "PENDING", <br>"orderedAt": "2025-04-25T18:53:02.636048"<br> }<br> ] | 200 OK
주문 수정 | PATCH | /orders/{orderId} |   | { <br>"orderId": 1,<br> "userId": 1234,<br> "storeName": "Pizza Palace", <br>"status": COOKING, <br>"items":<br>[<br> ….<br>} | 200 OK
주문 취소 | DELETE | /orders/{orderId} |   | orderStatus : PENDING→ CALCEL | 200 OK

### < 리뷰 >
기능 | HTTP Method | URL | Request | Response | status
-- | -- | -- | -- | -- | --
리뷰 작성 | POST | /orders/{orderId}/reviews | {<br> ”rating”:별점 <br>”content”:리뷰 <br>} | { <br>”reviewId”: 1,<br> ”storeName” : “가게명”, <br>”rating” : 5, <br>”content”: “맛있어요.”<br> ”createdAt”:”2025-04-25” <br>} | 201 CREATED
내 리뷰보기 | GET | /users/me/reviews |   | [ <br>{ <br>”reviewId”: 1,<br> ”storeName” : “가게명”, <br>”rating” : 5, <br>”content”: “맛있어요.” <br>”createdAt”:”2025-04-25” <br>}, <br>{<br> ”reviewId”: 2, <br>”storeName” : “가게명2”, <br>”rating” : 1, <br>”content”: “맛없어요.”<br> ”createdAt”:”2025-04-25”<br> } <br>] | 200 OK
가게 리뷰 조회 | GET | /stores/{storeId}/reviews |   | 위와 같음 | 200 OK
가게 리뷰 평점별로 조회 | GET | /stores/{storeId}/reviews",<br> params = {"minRating", "maxRating"} |   | 위와 같음 | 200 OK
리뷰 삭제 | DELETE | /reviews/{reviewId} |   |   | 200 OK

### < 광고 >
 기능            | HTTP Method | URL                          | Request                                                                                                             | Response                                                                                                                                 | status 
---------------|-------------|------------------------------|---------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|--------
 광고 생성         | POST        | /stores/{storeId}/ads        | {<br>	"startAt":"2025-04-28T14:30:00",<br>	"endAt":"2025-05-28T14:30:00",<br>	"isActive":true,<br>	"priority":1<br>} | {adId}                                                                                                                                   | 201 Created
 광고 상세         | GET         | /stores/{storeId}/ads/{adId} |                                                                                                                     | {<br>	"id":1,<br>	"storeId":3,<br>	"startAt":"2025-04-22T00:30:00",<br>	"endAt":"2025-05-28T23:30:00"<br>}                               | 200 OK
 상점 조회 (광고 포함) | GET         | /bff/store-list              | ?name=                                                                                                              | [{<br>"id": 7,<br>"name": "store1-1",<br>"openingTime": "08:00:00",<br>"closingTime": "22:00:00",<br>"minOrderValue": 0<br>}, {...}<br>] | 200 OK
