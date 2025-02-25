package org.example.login.dto;

public record AuthTokens (
        String accessToken,
        String refreshToken
) {
}
