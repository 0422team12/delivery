package org.example.delivery.domain.ad.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class OwnerMismatchException extends ErrorResponseException {

    // 요청자 Token의 userId와 Store의 owner가 일치하지 않는 경우.

    public OwnerMismatchException(){
        super(HttpStatus.FORBIDDEN);

        getBody().setDetail("해당 가게의 관리자가 아닙니다.");
    }

    public OwnerMismatchException(String detail) {
        super(HttpStatus.FORBIDDEN);
        getBody().setDetail(detail);

    }
}
