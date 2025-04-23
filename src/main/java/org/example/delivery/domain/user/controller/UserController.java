package org.example.delivery.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.delivery.domain.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}/deactivate")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }


}
