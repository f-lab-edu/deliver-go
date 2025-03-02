package org.deliverygo.login.service;

import lombok.RequiredArgsConstructor;
import org.deliverygo.global.jwt.JwtManager;
import org.deliverygo.login.dto.AuthTokens;
import org.deliverygo.login.dto.LoginDto;
import org.deliverygo.login.dto.UserDto;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.JwtRepository;
import org.deliverygo.login.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.deliverygo.login.constants.JwtProperties.REFRESH_EXPIRE_MINUTE;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtManager jwtManager;
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthTokens login(LoginDto loginDto) {

        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();
        user.verifyPassword(passwordEncoder, loginDto.getPassword());

        UserDto userDto = UserDto.of(user);
        AuthTokens authTokens = jwtManager.createAuthTokens(userDto);

        jwtRepository.insert(String.valueOf(userDto.getId()), authTokens.refreshToken(), REFRESH_EXPIRE_MINUTE);

        return authTokens;
    }
}
