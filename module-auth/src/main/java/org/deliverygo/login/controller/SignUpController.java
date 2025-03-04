package org.deliverygo.login.controller;

import lombok.RequiredArgsConstructor;
import org.deliverygo.login.dto.SignUpRequest;
import org.deliverygo.login.service.SignUpService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    @PostMapping("/auth/register")
    public void signUpUser(@Validated @RequestBody SignUpRequest request) {
        signUpService.signUp(request);
    }
}
