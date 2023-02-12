package com.ilkda.server.auth.controller;

import com.ilkda.server.auth.dto.LoginForm;
import com.ilkda.server.auth.dto.TokenDTO;
import com.ilkda.server.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ilkda.server.utils.api.ApiUtil.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public SuccessResponse<TokenDTO> login(@RequestBody LoginForm form) {
        return new SuccessResponse<>(authService.registerUser(form.getKakaoToken()));
    }

}
