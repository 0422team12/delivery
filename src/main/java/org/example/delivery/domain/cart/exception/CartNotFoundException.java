package org.example.delivery.domain.cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class CartNotFoundException extends ErrorResponseException {
    public CartNotFoundException(){
        super(HttpStatus.NOT_FOUND);
        ProblemDetail body = getBody(); // 부모가 자동 생성한 body에 접근
        body.setTitle("Cart Not Found");
        body.setDetail("장바구니가 비어있습니다.");
    }
}
