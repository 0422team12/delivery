package org.example.delivery.domain.ad.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class AdNotFoundException extends ErrorResponseException {

    // 요청자 Token의 userId와 Store의 owner가 일치하지 않는 경우.

    public AdNotFoundException(){
        super(HttpStatus.NOT_FOUND);

        getBody().setDetail("광고 정보를 찾을 수 없습니다.");
    }

    public AdNotFoundException(String detail) {
        super(HttpStatus.NOT_FOUND);

        getBody().setDetail(detail);
    }
}
