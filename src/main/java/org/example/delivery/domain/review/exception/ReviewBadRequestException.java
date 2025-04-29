package org.example.delivery.domain.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ReviewBadRequestException extends ErrorResponseException {
    public ReviewBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST);
        ProblemDetail body = getBody();
        body.setTitle("Order Forbidden");
        body.setDetail(message);
    }
}
