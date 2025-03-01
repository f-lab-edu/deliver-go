package org.deliverygo.login.service;

import jakarta.transaction.Transactional;
import org.deliverygo.MainApplication;
import org.deliverygo.login.dto.SignUpRequest;
import org.deliverygo.login.entity.User;
import org.deliverygo.login.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.deliverygo.login.constants.UserGrade.OWNER;

@ActiveProfiles("test")
@SpringBootTest(classes = MainApplication.class)
@Transactional
class SignUpServiceImplTest {

    @Autowired
    SignUpServiceImpl signUpService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원이 없으면 회원가입에 성공한다.")
    void userIsNotExistThenSignUpSuccess() {
        //given
        SignUpRequest signUpRequest = new SignUpRequest("js.min@google.com",
                "11112222",
                "민장식",
                "01020893750",
                "인천 서구 당하동",
                OWNER);

        //when
        signUpService.signUp(signUpRequest);
        User findUser = userRepository.findByEmail(signUpRequest.email()).orElseThrow();

        //then
        Assertions.assertEquals(signUpRequest.email(), findUser.getEmail());
    }
}
