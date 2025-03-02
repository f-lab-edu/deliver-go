package org.deliverygo.login.dto;

public record AuthTokens(

    String accessToken,
    String refreshToken
) {
}
