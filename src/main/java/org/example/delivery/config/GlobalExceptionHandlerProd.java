package org.example.delivery.config;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Profile("prod")
@RestControllerAdvice
public class GlobalExceptionHandlerProd {


    // Production 환경에서의 Error 방지
    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntime(RuntimeException e){
        if(e instanceof ErrorResponse){
            throw e;
        }

        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }


}
