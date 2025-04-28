package org.example.delivery.domain.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class OrderNotFoundException extends ErrorResponseException {
    public OrderNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND);
        ProblemDetail body = getBody();
        body.setTitle("Order Not Found");
        body.setDetail(message);

    }
}
