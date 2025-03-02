package org.deliverygo.login.service;

import lombok.RequiredArgsConstructor;
import org.deliverygo.login.domain.JwtManager;
import org.deliverygo.login.domain.LoginAuthenticator;
import org.deliverygo.login.dto.AuthTokens;
import org.deliverygo.login.dto.LoginRequest;
import org.deliverygo.login.dto.UserDto;
import org.deliverygo.login.repository.JwtRedisRepository;
import org.springframework.stereotype.Service;

import static org.deliverygo.login.constants.JwtProperties.REFRESH_EXPIRE_MINUTE;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginAuthenticator loginAuthenticator;
    private final JwtManager jwtManager;
    private final JwtRedisRepository jwtRedisRepository;

    public AuthTokens authenticateAndIssueTokens(LoginRequest loginRequest) {

        UserDto userDto = loginAuthenticator.authenticate(loginRequest);

        AuthTokens authTokens = jwtManager.createAuthTokens(userDto);

        jwtRedisRepository.insert(String.valueOf(userDto.getId()), authTokens.refreshToken(), REFRESH_EXPIRE_MINUTE);

        return authTokens;
    }
}
