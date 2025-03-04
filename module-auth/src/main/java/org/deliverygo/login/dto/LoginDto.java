package org.deliverygo.login.dto;

import lombok.Getter;

@Getter
public class LoginDto {

    private final String email;
    private final String password;

    private LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginDto of(LoginRequest loginRequest) {
        return new LoginDto(loginRequest.email(), loginRequest.password());
    }
}
