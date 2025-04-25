package org.example.delivery.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.common.JwtUtil;
import org.example.delivery.common.PasswordEncoder;
import org.example.delivery.domain.auth.dto.LoginResponseDto;
import org.example.delivery.domain.user.UserRepository;
import org.example.delivery.domain.user.entity.User;
import org.example.delivery.domain.user.enums.UserRole;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public void signup(String email, String password, UserRole userRole) {
        String encodePassword = passwordEncoder.encode(password);

        User saveUser = new User(email, encodePassword, userRole);

        userRepository.save(saveUser);
    }

    public LoginResponseDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 없음"));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호");
        }

        String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        return new LoginResponseDto(token);
    }
}
