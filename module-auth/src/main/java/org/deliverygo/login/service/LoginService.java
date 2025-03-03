package org.deliverygo.login.service;

import lombok.RequiredArgsConstructor;
import org.deliverygo.login.domain.JwtToken;
import org.deliverygo.login.dto.JwtTokenDto;
import org.deliverygo.login.dto.LoginDto;
import org.deliverygo.login.dto.UserDto;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.JwtRepository;
import org.deliverygo.login.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.deliverygo.login.domain.JwtToken.REFRESH_EXPIRE_MINUTE;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenDto login(LoginDto loginDto) {

        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();
        user.verifyPassword(passwordEncoder, loginDto.getPassword());

        UserDto userDto = UserDto.of(user);
        JwtToken accessToken = JwtToken.ofAccessToken(userDto);
        JwtToken refreshToken = JwtToken.ofRefreshToken(userDto);

        jwtRepository.insert(String.valueOf(userDto.getId()), refreshToken.getToken(), REFRESH_EXPIRE_MINUTE);

        return JwtTokenDto.of(accessToken.getToken(), refreshToken.getToken());
    }
}
