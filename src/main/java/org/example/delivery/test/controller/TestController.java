package org.example.delivery.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.delivery.test.exception.ExampleException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Profile("dev")
@RequestMapping("/test")
@Controller
public class TestController {


    @GetMapping("/exception")
    public ResponseEntity<?> exceptionTest(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String body,
            @RequestParam(required = false) Integer status
    ){

        if(title != null && body != null && status != null){
            throw new ExampleException(status, title, body);
        }

        if(title != null && body != null){
            throw new ExampleException(title, body);
        }

        if(title != null && status != null){
            throw new ExampleException(HttpStatusCode.valueOf(status), title);
        }

        if(title != null){
            throw new ExampleException(HttpStatus.OK, title);
        }

        throw new ExampleException();
    }

}
