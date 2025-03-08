package org.deliverygo.login.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

class JwtTokenTest {

    String SECRET_KEY = "prnlpoyiASttohnKeansocleSitnIcseascackiSceebaTAeu";
    SecretKey KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    @Test
    @DisplayName("jwt expire 만료 시간 유효한 경우 jwt 접근 성공")
    void jwtNotExpiredSuccess() {
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Date date = Date.from(fixedClock.instant());
        long expireMinute = 1L;

        String token = buildJwt(date, expireMinute);

        Assertions.assertEquals(Boolean.FALSE, isTokenExpired(token, createFixedPlusClock(fixedClock, 30)));
        Assertions.assertEquals(Boolean.FALSE, isTokenExpired(token, createFixedPlusClock(fixedClock, 50)));
        Assertions.assertEquals(Boolean.FALSE, isTokenExpired(token, createFixedPlusClock(fixedClock, 39)));
    }

    @Test
    @DisplayName("jwt expire 만료 시간 지난 경우 jwt 접근 실패")
    void jwtExpiredFail() {
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        Date date = Date.from(fixedClock.instant());
        long expireMinute = 1L;

        String token = buildJwt(date, expireMinute);

        Assertions.assertEquals(Boolean.TRUE, isTokenExpired(token, createFixedPlusClock(fixedClock, 65)));
        Assertions.assertEquals(Boolean.TRUE, isTokenExpired(token, createFixedPlusClock(fixedClock, 100)));
    }

    private String buildJwt(Date date, long expireMinute) {
        return Jwts.builder()
            .issuer("delivery-go")
            .subject(String.valueOf("testId"))
            .claim("email", "testEmail")
            .claim("name", "testName")
            .claim("grade", "OWNER")
            .issuedAt(date)
            .expiration(new Date((date.getTime() + (expireMinute * 1000 * 60))))
            .signWith(KEY)
            .compact();
    }

    private boolean isTokenExpired(String token, Clock clock) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
            return claims.getExpiration().before(Date.from(clock.instant()));
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private Clock createFixedPlusClock(Clock clock, int secondsToAdd) {
        return Clock.fixed(clock.instant().plusSeconds(secondsToAdd), ZoneId.systemDefault());
    }
}
