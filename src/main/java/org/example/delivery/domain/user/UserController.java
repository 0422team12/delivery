package org.example.delivery.domain.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class UserController {

    @GetMapping("/checkid")
    public Long checkId(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userId;
    }

    @GetMapping("/checkemail")
    public String checkemail(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return email;
    }
}
