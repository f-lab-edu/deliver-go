package org.deliverygo.login.domain;

import org.deliverygo.login.constants.UserGrade;
import org.deliverygo.login.dto.SignUpRequest;
import org.deliverygo.login.dto.UserDto;
import org.deliverygo.login.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.deliverygo.login.constants.UserGrade.NORMAL;
import static org.deliverygo.login.constants.UserGrade.OWNER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("jwt 에서 grade 값이 OWNER 이면 성공")
    void test1() {
        SignUpRequest signUpRequest = createGradeRequest(OWNER);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        User user = User.ofEncrypt(passwordEncoder, signUpRequest);
        JwtToken accessToken = JwtToken.ofAccessToken(UserDto.of(user));

        assertEquals(OWNER, accessToken.extractGrade());
    }

    @Test
    @DisplayName("jwt 에서 grade 값이 OWNER 가 아니면 실패")
    void test2() {
        SignUpRequest signUpRequest = createGradeRequest(NORMAL);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        User user = User.ofEncrypt(passwordEncoder, signUpRequest);
        JwtToken accessToken = JwtToken.ofAccessToken(UserDto.of(user));

        Assertions.assertNotEquals(OWNER, accessToken.extractGrade());
    }

    private SignUpRequest createGradeRequest(UserGrade grade) {
        return new SignUpRequest("js.min",
                "5757575"
                , "js.min"
                , "01122223333"
                , "강남구청"
                , grade);
    }
}
