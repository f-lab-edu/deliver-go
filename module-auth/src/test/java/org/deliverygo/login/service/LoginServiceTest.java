package org.deliverygo.login.service;

import org.deliverygo.MainApplication;
import org.deliverygo.login.constants.UserGrade;
import org.deliverygo.login.domain.JwtToken;
import org.deliverygo.login.dto.JwtTokenDto;
import org.deliverygo.login.dto.LoginDto;
import org.deliverygo.login.dto.LoginRequest;
import org.deliverygo.login.dto.SignUpRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.deliverygo.login.constants.UserGrade.OWNER;

@SpringBootTest(classes = MainApplication.class)
@Transactional
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Autowired
    SignUpService signUpService;

    @Test
    @DisplayName("이메일과 비밀번호가 일치하면 로그인 성공")
    void loginWhenValidCredentialsThenSuccess() {
        SignUpRequest signUpRequest = createSignUpRequest("js.min@hansol.com", "11112222",
                "민장식", "01011112222", "인천 서구 당하동", OWNER);
        signUpService.signUp(signUpRequest);
        LoginDto loginDto = LoginDto.of(new LoginRequest(signUpRequest.email(), signUpRequest.password()));

        JwtTokenDto jwtTokenDto = loginService.login(loginDto);

        JwtToken accessToken = JwtToken.of(jwtTokenDto.getAccessToken());
        JwtToken refreshToken = JwtToken.of(jwtTokenDto.getAccessToken());

        Assertions.assertEquals(signUpRequest.email(), accessToken.extractEmail());
        Assertions.assertEquals(signUpRequest.email(), refreshToken.extractEmail());
    }

    @Test
    @DisplayName("비밀번호 오류 시, 로그인 실패")
    void loginWhenInvalidPasswordThenThrowException() {
        SignUpRequest signUpRequest = createSignUpRequest("js.min@hansol.com", "11112222",
                "민장식", "01011112222", "인천 서구 당하동", OWNER);
        signUpService.signUp(signUpRequest);
        LoginDto loginDto = LoginDto.of(new LoginRequest(signUpRequest.email(), "22221111"));

        Assertions.assertThrows(IllegalStateException.class, () -> loginService.login(loginDto));
    }

    @Test
    @DisplayName("계정 없을 시, 로그인 실패")
    void loginWhenAccountNotFoundThenThrowException() {
        SignUpRequest signUpRequest = createSignUpRequest("js.min@hansol.com", "11112222",
                "민장식", "01011112222", "인천 서구 당하동", OWNER);
        signUpService.signUp(signUpRequest);
        LoginDto loginDto = LoginDto.of(new LoginRequest("jsggegws@hansol.com", signUpRequest.password()));

        Assertions.assertThrows(NoSuchElementException.class, () -> loginService.login(loginDto));
    }

    private SignUpRequest createSignUpRequest(String email, String password, String name, String phone,
                                              String address, UserGrade userGrade) {
        return new SignUpRequest(email, password, name, phone, address, userGrade);
    }
}
