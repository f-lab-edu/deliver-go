package org.deliverygo.login.domain;

import org.deliverygo.login.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static org.deliverygo.login.constants.UserGrade.OWNER;

class JwtTokenTest {

    UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.of(1L, "testName", "testEmail", "testAddress", "testPhone",
            OWNER);
    }

    @Test
    @DisplayName("jwt expire 만료 시간 유효한 경우 jwt 접근 성공")
    void jwtNotExpiredSuccess() {

        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Date date = Date.from(fixedClock.instant());

        JwtToken jwtToken = JwtToken.ofAccessToken(userDto, date, 1L);
        String token = jwtToken.getToken();

        Assertions.assertEquals(Boolean.FALSE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 30)));
        Assertions.assertEquals(Boolean.FALSE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 50)));
        Assertions.assertEquals(Boolean.FALSE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 39)));
    }

    @Test
    @DisplayName("jwt expire 만료 시간 지난 경우 jwt 접근 실패")
    void jwtExpiredFail() {
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Date date = Date.from(fixedClock.instant());
        JwtToken jwtToken = JwtToken.ofAccessToken(userDto, date, 1L);
        String token = jwtToken.getToken();

        Assertions.assertEquals(Boolean.TRUE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 65)));
        Assertions.assertEquals(Boolean.TRUE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 100)));
    }

    private Clock createFixedPlusClock(Clock clock, int secondsToAdd) {
        return Clock.fixed(clock.instant().plusSeconds(secondsToAdd), ZoneId.systemDefault());
    }
}
