package org.example.login.service;

import lombok.RequiredArgsConstructor;
import org.example.login.domain.JwtManager;
import org.example.login.domain.LoginAuthenticator;
import org.example.login.dto.AuthTokens;
import org.example.login.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl {

    private final LoginAuthenticator loginAuthenticator;
    private final JwtManager jwtManager;
//    private final RedisTemplate redisTemplate;

    public AuthTokens authenticateAndIssueTokens(String email, String password) {
        // validate
        UserDto userDto = loginAuthenticator.authenticate(email, password);

        // create jwt
        AuthTokens authTokens = jwtManager.createAuthTokens(userDto);


//        redisTemplate.insert(key, authTokens.refreshToken());

        return authTokens;
    }
}
