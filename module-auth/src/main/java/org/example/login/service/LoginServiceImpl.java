package org.example.login.service;

import static org.example.login.constants.JwtProperties.REFRESH_EXPIRE_MINUTE;

import lombok.RequiredArgsConstructor;
import org.example.login.domain.JwtManager;
import org.example.login.domain.LoginAuthenticator;
import org.example.login.dto.AuthTokens;
import org.example.login.dto.LoginRequest;
import org.example.login.dto.UserDto;
import org.example.login.repository.JwtRedisRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

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
