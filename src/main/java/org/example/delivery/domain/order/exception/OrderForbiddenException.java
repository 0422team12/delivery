package org.example.delivery.domain.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class OrderForbiddenException extends ErrorResponseException {
    public OrderForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN);
        ProblemDetail body = getBody();
        body.setTitle("Order Forbidden");
        body.setDetail(message);

    }
}