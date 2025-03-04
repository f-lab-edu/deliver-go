package org.deliverygo.login.dto;

import lombok.Getter;

@Getter
public class JwtTokenDto {
    private final String accessToken;
    private final String refreshToken;

    private JwtTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static JwtTokenDto of(String accessToken, String refreshToken) {
        return new JwtTokenDto(accessToken, refreshToken);
    }
}
