package org.example.delivery.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.auth.dto.LoginRequestDto;
import org.example.delivery.domain.auth.dto.LoginResponseDto;
import org.example.delivery.domain.auth.dto.SignupRequestDto;
import org.example.delivery.domain.auth.service.AuthService;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            authService.signup(requestDto.getEmail(), requestDto.getPassword(), requestDto.getUserRole());
        } catch(DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 사용된 이메일");
        }
        return new ResponseEntity<>("회원가입 완료", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Validated @RequestBody LoginRequestDto requestDto) {
        LoginResponseDto loginToken = authService.login(requestDto.getEmail(), requestDto.getPassword());

        return new ResponseEntity<>(loginToken, HttpStatus.OK);
    }

}
