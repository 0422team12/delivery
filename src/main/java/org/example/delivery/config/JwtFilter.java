package org.example.delivery.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.delivery.common.JwtUtil;
import org.example.delivery.domain.user.enums.UserRole;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
// http request 한번의 요청에 대해 한번만 실행하는 Filter
// 인증, 인가와 같이 한번만 거쳐야 하는 logic에 많이 사용
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private static final String[] WHITE_LIST = {
            "/login",
            "/signup",
            "/login/kakao",
            "/login/kakao/callback",
            "/auth/login",
            "/auth/signup"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String url = request.getRequestURI();

        if(isWhiteList(url)) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) { // 토큰이 비어있는지 확인
            throw new IllegalArgumentException("토큰 없음");
        }

        String token = bearerJwt.substring(7);

        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }

        Claims claims = jwtUtil.getClaims(token);

        UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

        request.setAttribute("userId", Long.parseLong(claims.getSubject()));
        request.setAttribute("email", claims.get("email"));
        request.setAttribute("userRole", claims.get("userRole"));

        chain.doFilter(request, response);
    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
