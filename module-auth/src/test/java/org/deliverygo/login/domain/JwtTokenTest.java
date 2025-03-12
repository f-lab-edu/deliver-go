package org.deliverygo.login.domain;

import org.deliverygo.login.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static org.deliverygo.login.constants.UserGrade.NORMAL;
import static org.deliverygo.login.constants.UserGrade.OWNER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

        assertEquals(Boolean.FALSE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 30)));
        assertEquals(Boolean.FALSE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 50)));
        assertEquals(Boolean.FALSE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 39)));
    }

    @Test
    @DisplayName("jwt expire 만료 시간 지난 경우 jwt 접근 실패")
    void jwtExpiredFail() {
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Date date = Date.from(fixedClock.instant());
        JwtToken jwtToken = JwtToken.ofAccessToken(userDto, date, 1L);
        String token = jwtToken.getToken();

        assertEquals(Boolean.TRUE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 65)));
        assertEquals(Boolean.TRUE, jwtToken.isExpired(token, createFixedPlusClock(fixedClock, 100)));
    }

    @Test
    @DisplayName("jwt 에서 grade 값이 OWNER 이면 성공")
    void extractGradeWhenOwner() {
        userDto = UserDto.of(1L, "testName", "testEmail", "testAddress", "testPhone",
            OWNER);
        JwtToken jwtToken = JwtToken.ofAccessToken(userDto, new Date(), 1L);

        assertEquals(OWNER, jwtToken.extractGrade());
    }

    @Test
    @DisplayName("jwt 에서 grade 값이 OWNER 가 아니면 실패")
    void extractGradeWhenNotOwner() {
        userDto = UserDto.of(1L, "testName", "testEmail", "testAddress", "testPhone",
            NORMAL);

        JwtToken jwtToken = JwtToken.ofAccessToken(userDto, new Date(), 1L);

        assertNotEquals(OWNER, jwtToken.extractGrade());
    }

    private Clock createFixedPlusClock(Clock clock, int secondsToAdd) {
        return Clock.fixed(clock.instant().plusSeconds(secondsToAdd), ZoneId.systemDefault());
    }
}
