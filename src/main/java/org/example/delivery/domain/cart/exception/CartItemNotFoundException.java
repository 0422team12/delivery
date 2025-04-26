package org.example.delivery.domain.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class CartItemNotFoundException extends ErrorResponseException {
    public CartItemNotFoundException(Long cartItemId){
        super(HttpStatus.NOT_FOUND);
        ProblemDetail body = getBody(); // 부모가 자동 생성한 body에 접근
        body.setTitle("CartItem Not Found");
        body.setDetail("cartItemId " + cartItemId + "에 해당하는 메뉴가 장바구니에 존재하지 않습니다.");
    }
}
