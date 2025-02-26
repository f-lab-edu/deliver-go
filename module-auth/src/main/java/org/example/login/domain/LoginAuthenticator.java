package org.example.login.domain;

import lombok.RequiredArgsConstructor;
import org.example.login.dto.LoginRequest;
import org.example.login.dto.UserDto;
import org.example.login.entity.User;
import org.example.login.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginAuthenticator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto authenticate(LoginRequest loginRequest) {
        User findUser = userRepository.findByEmail(loginRequest.email())
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(loginRequest.password(), findUser.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
        return UserDto.of(findUser);
    }
}
