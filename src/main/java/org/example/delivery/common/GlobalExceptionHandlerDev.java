package org.example.delivery.common;


import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Profile("dev")
@RestControllerAdvice
public class GlobalExceptionHandlerDev {

}
