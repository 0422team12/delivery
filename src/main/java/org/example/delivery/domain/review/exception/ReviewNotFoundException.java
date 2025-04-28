package org.example.delivery.domain.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ReviewNotFoundException extends ErrorResponseException {
    public ReviewNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND);
        ProblemDetail body = getBody();
        body.setTitle("Order Forbidden");
        body.setDetail(message);
    }
}