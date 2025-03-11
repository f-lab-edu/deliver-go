package org.deliverygo.login.service;

import org.deliverygo.MainApplication;
import org.deliverygo.login.domain.JwtToken;
import org.deliverygo.login.dto.JwtTokenDto;
import org.deliverygo.login.dto.LoginRequest;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.JwtRepository;
import org.deliverygo.login.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.deliverygo.login.constants.UserGrade.OWNER;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = MainApplication.class)
@Transactional
class LoginServiceTest {

    @MockBean
    JwtRepository jwtRepository;

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User user;
    String email;
    String originPassword;
    LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        originPassword = "11112222";
        email = "js.min@test.com";
        user = User.ofEncrypt(passwordEncoder, originPassword, "민장식", email,
            "01011112222", "인천 서구 당하동", OWNER);
        userRepository.save(user);
        loginRequest = new LoginRequest(email, originPassword);
    }

    @Test
    @DisplayName("이메일과 비밀번호가 일치하면 로그인 성공")
    void loginWhenValidCredentialsThenSuccess() {
        doNothing().when(jwtRepository).insertJwt(anyString(), anyString(), anyLong());

        JwtTokenDto jwtTokenDto = loginService.login(loginRequest);

        JwtToken accessToken = JwtToken.of(jwtTokenDto.getAccessToken());
        JwtToken refreshToken = JwtToken.of(jwtTokenDto.getAccessToken());

        Assertions.assertEquals(user.getEmail(), accessToken.extractEmail());
        Assertions.assertEquals(user.getEmail(), refreshToken.extractEmail());
    }

    @Test
    @DisplayName("비밀번호 오류 시, 로그인 실패")
    void loginWhenInvalidPasswordThenThrowException() {
        loginRequest = new LoginRequest(user.getEmail(), "22221111");
        Assertions.assertThrows(IllegalStateException.class, () -> loginService.login(loginRequest));
    }

    @Test
    @DisplayName("계정 없을 시, 로그인 실패")
    void loginWhenAccountNotFoundThenThrowException() {
        userRepository.findByEmail(email).ifPresent(userRepository::delete);

        Assertions.assertThrows(NoSuchElementException.class, () -> loginService.login(loginRequest));
    }
}
