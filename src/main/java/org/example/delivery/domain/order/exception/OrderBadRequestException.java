package org.example.delivery.domain.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class OrderBadRequestException extends ErrorResponseException {
    public OrderBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST);
        ProblemDetail body = getBody();
        body.setTitle("Order Not Found");
        body.setDetail(message);

    }
}