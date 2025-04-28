package org.example.delivery.Auth;

import org.example.delivery.common.PasswordEncoder;
import org.example.delivery.domain.auth.service.AuthService;
import org.example.delivery.domain.user.UserRepository;
import org.example.delivery.domain.user.entity.User;
import org.example.delivery.domain.user.enums.UserRole;
import org.example.delivery.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.delivery.domain.user.enums.UserRole.USER;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void signup_메서드가_정상동작() {
        // given
        String email = "jungnam9742@naver.com";
        String rawPassword = "Password123!";
        UserRole userRole = USER;
        String encodedPassword = passwordEncoder.encode(rawPassword);
        //when
        User user = new User(email, encodedPassword, userRole);
        //then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(encodedPassword);
        assertThat(user.getUserRole()).isEqualTo(userRole);
    }

}
