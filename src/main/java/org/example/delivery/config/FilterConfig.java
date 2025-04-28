package org.example.delivery.config;

import lombok.RequiredArgsConstructor;
import org.example.delivery.common.JwtUtil;
import org.example.delivery.domain.user.UserRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtUtil));
        registrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴을 지정합니다.

        return registrationBean;
    }

}
