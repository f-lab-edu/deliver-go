package org.deliverygo.login.service;

import lombok.RequiredArgsConstructor;
import org.deliverygo.login.domain.JwtToken;
import org.deliverygo.login.dto.JwtTokenDto;
import org.deliverygo.login.dto.LoginRequest;
import org.deliverygo.login.dto.UserDto;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.JwtRepository;
import org.deliverygo.login.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.deliverygo.login.domain.JwtToken.REFRESH_EXPIRE_MINUTE;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenDto login(LoginRequest loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow();
        user.verifyPassword(passwordEncoder, loginRequest);

        UserDto userDto = UserDto.of(user);

        Date date = new Date();
        JwtToken accessToken = JwtToken.ofAccessToken(userDto, date);
        JwtToken refreshToken = JwtToken.ofRefreshToken(userDto, date);

        jwtRepository.insertJwt(String.valueOf(userDto.getId()), refreshToken.getToken(), REFRESH_EXPIRE_MINUTE);

        return JwtTokenDto.of(accessToken.getToken(), refreshToken.getToken());
    }
}
