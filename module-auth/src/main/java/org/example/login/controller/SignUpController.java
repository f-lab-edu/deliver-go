package org.example.login.controller;

import lombok.RequiredArgsConstructor;
import org.example.login.dto.SignUpRequest;
import org.example.login.service.SignUpService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> signUpUser(@Validated @RequestBody SignUpRequest request) {

        signUpService.signUp(request);

        return ResponseEntity.ok().build();
    }
}
