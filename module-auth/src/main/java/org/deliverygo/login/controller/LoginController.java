package org.deliverygo.login.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.deliverygo.login.dto.JwtTokenDto;
import org.deliverygo.login.dto.LoginRequest;
import org.deliverygo.login.service.LoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/auth/login")
    public void login(@Validated @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = loginService.login(loginRequest);

        response.setHeader(HttpHeaders.AUTHORIZATION, jwtTokenDto.getAccessToken());
        response.setHeader("refresh-token", jwtTokenDto.getRefreshToken());
    }
}
