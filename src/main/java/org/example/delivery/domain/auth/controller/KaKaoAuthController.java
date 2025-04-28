package org.example.delivery.domain.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.auth.dto.KakaoUser;
import org.example.delivery.domain.auth.dto.KakaoUserRequestDto;
import org.example.delivery.domain.auth.dto.LoginResponseDto;
import org.example.delivery.domain.auth.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequiredArgsConstructor
public class KaKaoAuthController {
    private final KakaoAuthService kaKaoAuthService;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    //카카오 로그인 페이지 호출, 인가 코드 발급 요청
    @GetMapping("/login/kakao")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        //로그인 성공 후 돌아올 URI
        String encodeRedirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        //카카오 로그인 URL 동적 생성
        String kakaoUrl = "https://kauth.kakao.com/oauth/authorize" //인가 코드 요청 API
                + "?response_type=code" //OAuth 2.0 인증코드 방식
                + "&client_id=" + clientId
                + "&redirect_uri=" + encodeRedirectUri;

        response.sendRedirect(kakaoUrl);
    }

    //인가 코드로 토큰 발급 요청
    @GetMapping("/login/kakao/callback")
    public ResponseEntity<LoginResponseDto> kakaoCallback(
            @RequestParam("code") String code
    ) {
        String accessToken = kaKaoAuthService.kakaoLogin(code);

        KakaoUser kakaoUser = kaKaoAuthService.getUserInfo(accessToken);

        return new ResponseEntity<>(kaKaoAuthService.findOrCreateUser(kakaoUser), HttpStatus.OK);
    }
}
