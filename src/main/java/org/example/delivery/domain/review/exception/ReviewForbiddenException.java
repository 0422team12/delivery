package org.example.delivery.domain.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ReviewForbiddenException extends ErrorResponseException {
    public ReviewForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN);
        ProblemDetail body = getBody();
        body.setTitle("Order Forbidden");
        body.setDetail(message);
    }
}