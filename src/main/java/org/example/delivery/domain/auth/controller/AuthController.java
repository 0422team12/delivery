package org.example.delivery.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.auth.dto.LoginRequestDto;
import org.example.delivery.domain.auth.dto.LoginResponseDto;
import org.example.delivery.domain.auth.dto.SignupRequestDto;
import org.example.delivery.domain.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Validated @RequestBody SignupRequestDto requestDto) {
        authService.signup(requestDto.getEmail(), requestDto.getPassword(), requestDto.getUserRole());

        return new ResponseEntity<>("회원가입 완료", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Validated @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto loginToken = authService.login(requestDto.getEmail(), requestDto.getPassword());

        return new ResponseEntity<>(loginToken, HttpStatus.OK);
    }

}
