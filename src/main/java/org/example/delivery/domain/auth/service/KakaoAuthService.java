package org.example.delivery.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.common.JwtUtil;
import org.example.delivery.domain.auth.dto.KakaoUser;
import org.example.delivery.domain.auth.dto.KakaoUserRequestDto;
import org.example.delivery.domain.auth.dto.LoginResponseDto;
import org.example.delivery.domain.user.UserRepository;
import org.example.delivery.domain.user.entity.User;
import org.example.delivery.domain.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final JwtUtil jwtUtil;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";


    // 인가 코드를 이용해 액세스 토큰 요청
    public String kakaoLogin(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 파라미터
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        //요청 Entity를 만들어서 POST 요청을 보내 카카오 엑세스 토큰을 받아온다.
        //postForEntity-POST 요청을 보낸다.
        ResponseEntity<Map> response = restTemplate.postForEntity(
                KAKAO_TOKEN_URI,
                request,
                Map.class
        );

        try {
            Map<String, Object> responseBody = response.getBody();
            return (String) responseBody.get("access_token");
        } catch (Exception e) {
            throw new RuntimeException("액세스 토큰 파싱 실패", e);
        }
    }

    //유저 정보 가져오기
    public KakaoUser getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                KAKAO_USER_INFO_URI,
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");

        String email = (String) kakaoAccount.get("email");

        if (email == null) {
            throw new IllegalArgumentException("카카오 계정에 이메일이 없습니다.");
        }

        return new KakaoUser(email);
    }

    public LoginResponseDto findOrCreateUser(KakaoUser kakaoUser) {
        //유저의 존재 여부 확인 없으면 새로 생성 후, 저장
        User user = userRepository.findByEmail(kakaoUser.getEmail())
                .orElseGet(() -> {
                    String randomPassword = UUID.randomUUID().toString();
                    //현재 API로는 requestBody로 UserRole을 받아올 수 없어 우선 USER로 설정함
                    User newUser = new User(kakaoUser.getEmail(), randomPassword, UserRole.USER);
                    return userRepository.save(newUser);
                });

        //배달앱에서 유효한 토큰 발급
        String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new LoginResponseDto(token);
    }
}
